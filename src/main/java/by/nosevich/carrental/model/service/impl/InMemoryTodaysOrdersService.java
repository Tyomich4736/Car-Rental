package by.nosevich.carrental.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.orderenums.Status;
import by.nosevich.carrental.model.service.MailService;
import by.nosevich.carrental.model.service.OrdersControlService;
import by.nosevich.carrental.model.service.TodaysOrdersService;
import by.nosevich.carrental.model.service.entityservice.OrderService;

@Service
@Transactional 
public class InMemoryTodaysOrdersService implements TodaysOrdersService, InitializingBean{

	@Autowired
	private OrderService orderService;
	@Autowired
	private MailService mailService;
	@Autowired
	private OrdersControlService ordersControlService;
	
	private List<Order> todaysOrders = new ArrayList<Order>(); 

	public List<Order> getTodaysOrders() {
		return todaysOrders;
	}

	@Override
	@Scheduled(cron = "0 0 * * *")
	public void updateTodaysOrders() {
		cancelTodaysWaitingOrders();
		addNewOrdersInList();
	}

	private void cancelTodaysWaitingOrders() {
		for(Order order : todaysOrders) {
			if (order.getStatus()==Status.WAITING) {
				ordersControlService.cancelOrder(order);
			}
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
	public void removeFromTodaysOrders(Order order) {
		todaysOrders.remove(order);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		updateTodaysOrders();
	}
}
