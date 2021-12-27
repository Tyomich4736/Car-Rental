package by.nosevich.carrental.service.modelservice;

import by.nosevich.carrental.entities.Car;
import by.nosevich.carrental.entities.Order;
import by.nosevich.carrental.entities.User;
import by.nosevich.carrental.entities.orderenums.Status;

import java.util.Date;
import java.util.List;

public interface OrderService extends DAO<Order> {
    List<Order> getAllByCar(Car car);

    List<Order> getAllByUser(User user);

    void calculateAndSetPrice(Order order);

    List<Order> getAllByBeginDateAndStatus(Date beginDate, Status status);

    List<Order> getAllByEndDateAndStatus(Date endDate, Status status);

    Order getByStatusAndUser(Status status, User user);
}
