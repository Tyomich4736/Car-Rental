package by.nosevich.carrental.model.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class InMemoryTodaysOrdersService implements TodaysOrdersService{

	@Autowired
	private OrderService orderService;
	@Autowired
	private MailService mailService;
	
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
				cancelOrder(order);
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
	public void cancelOrder(Order order) {
		order.setStatus(Status.CANCELED);
		orderService.save(order);
		todaysOrders.remove(order);
		try {
			mailService.sendCanselOrderMessage(order.getUser(), order);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void activateOrder(Order order) {
		order.setStatus(Status.ACTIVE);
		orderService.save(order);
		todaysOrders.remove(order);
	}

	@Override
	public void finishOrder(Order order) {
		order.setStatus(Status.ENDED);
		orderService.save(order);
		todaysOrders.remove(order);
	}

	@Override
	public void waitOrder(Order order) {
		order.setStatus(Status.WAITING);
		orderService.save(order);
//		try {
//			mailService.sendSuccessfulOrderingMessage(currentUser, order);
//		} catch (MessagingException e) {
//			orderService.delete(order); 
//			return "redirect:/order/myOrders";
//		}
//	TODO uncomment this strings in final version
		if (dateRemoveTime(new Date()).compareTo(dateRemoveTime(order.getBeginDate()))==0)
			addToTodaysOrders(order);
	}
	
	private static Date dateRemoveTime(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

}
