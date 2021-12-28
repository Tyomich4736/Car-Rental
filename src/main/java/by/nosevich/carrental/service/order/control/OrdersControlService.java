package by.nosevich.carrental.service.order.control;

import by.nosevich.carrental.entities.Order;

import javax.mail.MessagingException;
import java.util.List;

public interface OrdersControlService {
    void waitOrder(Order order) throws MessagingException;

    void cancelOrder(Order order);

    void activateOrder(Order order);

    void finishOrder(Order order);

    Order saveUnconfirmedOrder(List<String> accessoryNames, String beginDateStr, String endDateStr, Integer carId, String username)
    throws Exception;
}
