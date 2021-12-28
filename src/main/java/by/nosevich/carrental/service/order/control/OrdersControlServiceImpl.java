package by.nosevich.carrental.service.order.control;

import by.nosevich.carrental.model.Accessory;
import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Order;
import by.nosevich.carrental.model.User;
import by.nosevich.carrental.model.enums.OrderStatus;
import by.nosevich.carrental.exceptions.OrderIsCrossException;
import by.nosevich.carrental.service.order.current.CurrentOrdersService;
import by.nosevich.carrental.service.accessory.AccessoryService;
import by.nosevich.carrental.service.car.CarService;
import by.nosevich.carrental.service.mailsender.MailService;
import by.nosevich.carrental.service.order.OrderService;
import by.nosevich.carrental.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

@Service
public class OrdersControlServiceImpl implements OrdersControlService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MailService mailService;
    @Autowired
    private CurrentOrdersService todaysOrders;
    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccessoryService accessoryService;

    @Override
    public void cancelOrder(Order order) {
        order.setOrderStatus(OrderStatus.CANCELED);
        orderService.save(order);
        todaysOrders.remove(order);
        try {
            mailService.sendCanselOrderMessage(order.getUser(), order);
        } catch(MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void activateOrder(Order order) {
        order.setOrderStatus(OrderStatus.ACTIVE);
        orderService.save(order);
        todaysOrders.remove(order);
    }

    @Override
    public void finishOrder(Order order) {
        order.setOrderStatus(OrderStatus.ENDED);
        orderService.save(order);
        todaysOrders.remove(order);
    }

    @Override
    public void waitOrder(Order order) throws MessagingException {
        order.setOrderStatus(OrderStatus.WAITING);
        orderService.save(order);
        mailService.sendSuccessfulOrderingMessage(order.getUser(), order);
        if(dateRemoveTime(new Date()).compareTo(dateRemoveTime(order.getBeginDate())) == 1)
            todaysOrders.addToTodaysOrders(order);
    }

    private static Date dateRemoveTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    @Override
    public Order saveUnconfirmedOrder(List<String> accessoryNames, String beginDateStr, String endDateStr, Integer carId, String username)
    throws Exception {
        // init and check values
        Car car = carService.getById(carId);
        Date beginDate, endDate;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        beginDate = format.parse(beginDateStr);
        endDate = format.parse(endDateStr);
        if(car == null) throw new RuntimeException();
        if(orderIsCross(beginDate, endDate, car)) {
            throw new OrderIsCrossException();
        }
        HashSet<Accessory> accessories = new HashSet<Accessory>();
        if(accessoryNames != null) accessoryNames.forEach(name -> {
            Accessory currentAccessory = accessoryService.getByName(name);
            if(currentAccessory != null) {
                accessories.add(currentAccessory);
                accessoryService.save(currentAccessory);
            }
        });

        Order order = new Order();
        order.setBeginDate(beginDate);
        order.setEndDate(endDate);
        order.setCar(car);
        order.setOrderStatus(OrderStatus.UNCOMFIRMED);
        User currentUser = userService.getByEmail(username);
        order.setUser(currentUser);

        orderService.save(order);
        order.setAccessories(accessories);
        orderService.calculateAndSetPrice(order);

        orderService.save(order);
        return order;
    }

    private boolean orderIsCross(Date beginDate, Date endDate, Car car) {
        List<Order> orders = orderService.getAllByCar(car);
        for(Order order : orders) {
            if((beginDate.compareTo(order.getBeginDate()) > 0 && beginDate.compareTo(order.getEndDate()) < 0) ||
                    ((endDate.compareTo(order.getBeginDate()) > 0 && endDate.compareTo(order.getEndDate()) < 0)) ||
                    (beginDate.compareTo(order.getBeginDate()) < 0 && endDate.compareTo(order.getEndDate()) > 0))
                return true;
        }
        return false;
    }
}
