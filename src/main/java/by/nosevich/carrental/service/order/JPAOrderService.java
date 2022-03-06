package by.nosevich.carrental.service.order;

import by.nosevich.carrental.dto.OrderDto;
import by.nosevich.carrental.dto.UserDto;
import by.nosevich.carrental.exceptions.OrderIsCrossException;
import by.nosevich.carrental.model.Accessory;
import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Order;
import by.nosevich.carrental.model.User;
import by.nosevich.carrental.model.enums.OrderStatus;
import by.nosevich.carrental.repository.AccessoryRepository;
import by.nosevich.carrental.repository.CarRepository;
import by.nosevich.carrental.repository.OrderRepository;
import by.nosevich.carrental.repository.UserRepository;
import by.nosevich.carrental.service.mailsender.MailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class JPAOrderService implements OrderService {

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccessoryRepository accessoryRepository;
    @Autowired
    private MailService mailService;

    @Override
    public List<OrderDto> getAll() {
        return orderRepository.findAll().stream().map(OrderDto::new).collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDto> getById(int id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(OrderDto::new);
    }

    @Override
    public void delete(OrderDto entity) {
        orderRepository.delete(convertDtoToEntity(entity));
    }

    @Override
    public void save(OrderDto entity) {
        orderRepository.save(convertDtoToEntity(entity));
    }

    @Override
    public List<OrderDto> getAllBusyByCar(Integer carId) {
        List<Order> results = orderRepository.findByCarAndStatuses(carId, OrderStatus.ACTIVE, OrderStatus.WAITING);
        return results.stream().map(OrderDto::new).collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDto> saveUnconfirmedOrder(List<Integer> accessoryIds,
                                                   String beginDateStr,
                                                   String endDateStr,
                                                   Integer carId,
                                                   String username) throws ParseException {
        Optional<Car> optionalCar = carRepository.findById(carId);
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (!optionalCar.isPresent() || !optionalUser.isPresent()) {
            return Optional.empty();
        }
        Car car = optionalCar.get();
        Date beginDate = DATE_FORMAT.parse(beginDateStr);
        Date endDate = DATE_FORMAT.parse(endDateStr);
        if (isOrderCross(beginDate, endDate, car.getId())) {
            throw new OrderIsCrossException();
        }
        HashSet<Accessory> accessories = new HashSet<>();
        if (accessoryIds != null) {
            List<Accessory> foundAccessories = accessoryRepository.findByIds(accessoryIds);
            accessories.addAll(foundAccessories);
        }
        Order order = new Order();
        order.setBeginDate(beginDate);
        order.setEndDate(endDate);
        order.setCar(car);
        order.setOrderStatus(OrderStatus.UNCONFIRMED);
        order.setUser(optionalUser.get());
        order.setAccessories(accessories);
        orderRepository.save(order);
        calculateAndSetPrice(order);
        return Optional.of(new OrderDto(order));
    }

    @Override
    public void confirmUnconfirmedOrder(String userEmail) {
        Optional<Order> optionalOrder = orderRepository.findByUserAndStatus(userEmail, OrderStatus.UNCONFIRMED);
        if (!optionalOrder.isPresent()) {
            return;
        }
        Order order = optionalOrder.get();
        order.setOrderStatus(OrderStatus.WAITING);
        orderRepository.save(order);
        try {
            mailService.sendSuccessfulOrderingMessage(new UserDto(order.getUser()), new OrderDto(order));
            if (getDateWithoutTime(new Date()).compareTo(getDateWithoutTime(order.getBeginDate())) > 0)
                mailService.sendPickUpOrderMessage(new UserDto(order.getUser()), new OrderDto(order));
        } catch(MessagingException e) {
            e.printStackTrace();
        }
        orderRepository.save(order);
    }

    @Override
    public void clearUnconfirmedOrderForUser(String userEmail) {
        Optional<Order> previousUnconfirmedOrder =
                orderRepository.findByUserAndStatus(userEmail, OrderStatus.UNCONFIRMED);
        previousUnconfirmedOrder.ifPresent(order -> orderRepository.delete(order));
    }

    @Override
    public List<OrderDto> getAllByUser(String userEmail) {
        return orderRepository.findAllByUserEmail(userEmail).stream().map(OrderDto::new).collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAllByUser(Integer userId) {
        return orderRepository.findAllByUserId(userId).stream().map(OrderDto::new).collect(Collectors.toList());
    }

    @Override
    public void cancelOrder(Integer orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
            try {
                mailService.sendCancelOrderMessage(new UserDto(order.getUser()), new OrderDto(order));
            } catch(MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void activateOrder(Integer orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(OrderStatus.ACTIVE);
            orderRepository.save(order);
        }
    }

    @Override
    public void finishOrder(Integer orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(OrderStatus.ENDED);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderDto> getTodaysOrders() {
        Date today = new Date();
        List<Order> orders = orderRepository.findByDateAndStatuses(today, OrderStatus.WAITING, OrderStatus.ACTIVE);
        return orders.stream().map(OrderDto::new).collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?", zone = "Europe/Minsk") //each day in 00.00
    public void sendTodaysPickUpMessages() {
        Date today = new Date();
        List<Order> orders = orderRepository.findByDateAndStatuses(today, OrderStatus.WAITING);
        orders.forEach(order -> {
            try {
                mailService.sendPickUpOrderMessage(new UserDto(order.getUser()), new OrderDto(order));
            } catch(MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    private void calculateAndSetPrice(Order order) {
        double price = 0;
        int days =
                Period.between(convertDateToLocalDate(order.getBeginDate()), convertDateToLocalDate(order.getEndDate()))
                        .getDays();
        if (days <= 3) {
            price += order.getCar().getPriceFrom1To3Days();
        } else if (days <= 7) {
            price += order.getCar().getPriceFrom4To7Days();
        } else if (days <= 15) {
            price += order.getCar().getPriceFrom8To15Days();
        } else if (days <= 30) {
            price += order.getCar().getPriceFrom16To30Days();
        }
        for (Accessory accessory : order.getAccessories()) {
            price += accessory.getPrice().doubleValue();
        }
        order.setPrice(price);
    }

    private LocalDate convertDateToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private boolean isOrderCross(Date beginDate, Date endDate, Integer carId) {
        List<Order> orders = orderRepository.findByCarAndStatuses(carId, OrderStatus.ACTIVE, OrderStatus.WAITING);
        for (Order order : orders) {
            if ((beginDate.compareTo(order.getBeginDate()) > 0 && beginDate.compareTo(order.getEndDate()) < 0) ||
                    ((endDate.compareTo(order.getBeginDate()) > 0 && endDate.compareTo(order.getEndDate()) < 0)) ||
                    (beginDate.compareTo(order.getBeginDate()) < 0 && endDate.compareTo(order.getEndDate()) > 0))
                return true;
        }
        return false;
    }

    private static Date getDateWithoutTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    private Order convertDtoToEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getId() != null) {
            Optional<Order> optionalEntity = orderRepository.findById(dto.getId());
            if (optionalEntity.isPresent()) {
                Order entity = optionalEntity.get();
                BeanUtils.copyProperties(dto, entity);
                return entity;
            }
        }
        Order entity = new Order();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
