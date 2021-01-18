package by.nosevich.carrental.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.orderenums.Status;
import by.nosevich.carrental.model.service.TodaysOrdersService;
import by.nosevich.carrental.model.service.entityservice.OrderService;
import lombok.Data;

@Service
@Data
public class InMemoryTodaysOrdersService implements TodaysOrdersService{

	@Autowired
	private OrderService orderService;
	
	private List<Order> todaysOrders = new ArrayList<Order>(); 

	@Override
	public void updateTodaysOrders() {
		cancelTodaysWaitingOrders();
		addNewOrdersInList();
	}

	private void cancelTodaysWaitingOrders() {
		for(Order order : todaysOrders) {
			if (order.getStatus()==Status.WAITING)
				order.setStatus(Status.CANCELED);
		}
	}
	
	private void addNewOrdersInList() {
		Date today = new Date();
		
		todaysOrders.addAll(orderService.getAllByBeginDateAndStatus(today, Status.WAITING));
		todaysOrders.addAll(orderService.getAllByEndDateAndStatus(today, Status.ACTIVE));
	}
	
	@Override
	public void addToTodaysOrders(Order order) {
		todaysOrders.add(order);
	}

	@Override
	public void deleteFromTodaysOrders(Order order) {
		todaysOrders.remove(order);
	}

}
