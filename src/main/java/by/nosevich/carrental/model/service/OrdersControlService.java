package by.nosevich.carrental.model.service;

import java.util.List;

import javax.mail.MessagingException;

import by.nosevich.carrental.model.entities.Order;

public interface OrdersControlService {
	void waitOrder(Order order) throws MessagingException;
	void cancelOrder(Order order);
	void activateOrder(Order order);
	void finishOrder(Order order);
	Order saveUnconfirmedOrder(List<String> accessoryNames, String beginDateStr, String endDateStr,
			Integer carId, String username) throws Exception;
}
