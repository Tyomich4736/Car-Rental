package by.nosevich.carrental.model.service;

import java.util.List;

import by.nosevich.carrental.model.entities.Order;

public interface TodaysOrdersService {
	List<Order> getTodaysOrders();
	void updateTodaysOrders();
	void addToTodaysOrders(Order order);
	void remove(Order order);
}
