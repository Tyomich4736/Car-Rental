package by.nosevich.carrental.service.order.current;

import by.nosevich.carrental.entities.Order;

import java.util.List;

public interface CurrentOrdersService {
    List<Order> getTodaysOrders();

    void updateTodaysOrders();

    void addToTodaysOrders(Order order);

    void remove(Order order);
}
