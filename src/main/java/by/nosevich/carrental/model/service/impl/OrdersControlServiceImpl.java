package by.nosevich.carrental.model.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.nosevich.carrental.exceptions.OrderIsCrossException;
import by.nosevich.carrental.model.entities.Accessory;
import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.orderenums.Status;
import by.nosevich.carrental.model.service.MailService;
import by.nosevich.carrental.model.service.OrdersControlService;
import by.nosevich.carrental.model.service.TodaysOrdersService;
import by.nosevich.carrental.model.service.entityservice.AccessoryService;
import by.nosevich.carrental.model.service.entityservice.CarService;
import by.nosevich.carrental.model.service.entityservice.OrderService;
import by.nosevich.carrental.model.service.entityservice.UserService;

@Service
public class OrdersControlServiceImpl implements OrdersControlService{
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private MailService mailService;
	@Autowired
	private TodaysOrdersService todaysOrders;
	@Autowired
	private CarService carService;
	@Autowired
	private UserService userService;
	@Autowired
	private AccessoryService accessoryService;
	
	@Override
	public void cancelOrder(Order order) {
		order.setStatus(Status.CANCELED);
		orderService.save(order);
		todaysOrders.remove(order);
		try {
			mailService.sendCanselOrderMessage(order.getUser(), order);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void activateOrder(Order order) {
		order.setStatus(Status.ACTIVE);
		orderService.save(order);
		todaysOrders.remove(order);
	}

	@Override
	public void finishOrder(Order order) {
		order.setStatus(Status.ENDED);
		orderService.save(order);
		todaysOrders.remove(order);
	}

	@Override
	public void waitOrder(Order order) throws MessagingException {
		order.setStatus(Status.WAITING);
		orderService.save(order);
		mailService.sendSuccessfulOrderingMessage(order.getUser(), order);
		if (dateRemoveTime(new Date()).compareTo(dateRemoveTime(order.getBeginDate()))==1)
			todaysOrders.addToTodaysOrders(order);
	}
	
	private static Date dateRemoveTime(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
	
	@Override
	public Order saveUnconfirmedOrder(List<String> accessoryNames, String beginDateStr, String endDateStr,
			Integer carId, String username) throws Exception{
		// init and check values
		Car car = carService.getById(carId);
		Date beginDate, endDate;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		beginDate = format.parse(beginDateStr);
		endDate = format.parse(endDateStr);
		if (car == null)
			throw new RuntimeException();
		if (orderIsCross(beginDate, endDate, car)) {
			throw new OrderIsCrossException();
		}
		HashSet<Accessory> accessories = new HashSet<Accessory>();
		if (accessoryNames != null)
			accessoryNames.forEach(name -> {
				Accessory currentAccessory = accessoryService.getByName(name);
				if (currentAccessory != null) {
					accessories.add(currentAccessory);
					accessoryService.save(currentAccessory);
				}
			});
		
		// creating order
		Order order = new Order();
		order.setBeginDate(beginDate);
		order.setEndDate(endDate);
		order.setCar(car);
		order.setStatus(Status.UNCOMFIRMED);
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
		for (Order order : orders) {
			if ((beginDate.compareTo(order.getBeginDate()) > 0 && beginDate.compareTo(order.getEndDate()) < 0)
					|| ((endDate.compareTo(order.getBeginDate()) > 0 && endDate.compareTo(order.getEndDate()) < 0))
					|| (beginDate.compareTo(order.getBeginDate())<0 && endDate.compareTo(order.getEndDate()) > 0))
				return true;
		}
		return false;
	}
}
