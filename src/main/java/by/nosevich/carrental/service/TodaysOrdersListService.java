package by.nosevich.carrental.service;

import by.nosevich.carrental.entities.Order;

import java.util.List;

public interface TodaysOrdersListService {
    List<Order> getTodaysOrders();

    void updateTodaysOrders();

    void addToTodaysOrders(Order order);

    void remove(Order order);
}
