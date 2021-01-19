package by.nosevich.carrental.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.orderenums.Status;
import by.nosevich.carrental.model.service.MailService;
import by.nosevich.carrental.model.service.TodaysOrdersService;
import by.nosevich.carrental.model.service.entityservice.OrderService;
import lombok.Data;

@Service
@Data
public class InMemoryTodaysOrdersService implements TodaysOrdersService{

	@Autowired
	private OrderService orderService;
	@Autowired
	private MailService mailService;
	
	private List<Order> todaysOrders = new ArrayList<Order>(); 

	@Override
	@Scheduled(cron = "")
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
		
		for (Order order : orderService.getAllByBeginDateAndStatus(today, Status.WAITING)) {
			addToTodaysOrders(order);
		}
		todaysOrders.addAll(orderService.getAllByEndDateAndStatus(today, Status.ACTIVE));
	}
	
	@Override
	public void addToTodaysOrders(Order order){
		todaysOrders.add(order);
		try {
			mailService.sendPickUpOrderMessage(order.getUser(), order);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteFromTodaysOrders(Order order) {
		todaysOrders.remove(order);
	}

}
