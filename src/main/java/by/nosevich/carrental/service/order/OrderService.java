package by.nosevich.carrental.service.order;

import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Order;
import by.nosevich.carrental.model.User;
import by.nosevich.carrental.model.enums.OrderStatus;
import by.nosevich.carrental.service.DaoService;

import java.util.Date;
import java.util.List;

public interface OrderService extends DaoService<Order> {
    List<Order> getAllByCar(Car car);

    List<Order> getAllByUser(User user);

    void calculateAndSetPrice(Order order);

    List<Order> getAllByBeginDateAndStatus(Date beginDate, OrderStatus orderStatus);

    List<Order> getAllByEndDateAndStatus(Date endDate, OrderStatus orderStatus);

    Order getByStatusAndUser(OrderStatus orderStatus, User user);
}
