package by.nosevich.carrental.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.orderenums.Status;
import by.nosevich.carrental.model.service.TodaysOrdersService;
import by.nosevich.carrental.model.service.entityservice.OrderService;
import by.nosevich.carrental.model.service.entityservice.UserService;

@Controller
@RequestMapping("/orders")
public class OrderListController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private TodaysOrdersService todaysOrdersService;
	
	@GetMapping("/user/{id}")
	public String getOrdersForUser(Model model, @PathVariable("id") Integer userId) {
		User user = userService.getById(userId);
		List<Order> orders = orderService.getAllByUser(user);
		model.addAttribute("orders", orders);
		return "orderList";
	}
	
	@GetMapping("/cancel/{id}")
	public String cancelOrder(HttpServletRequest req, @PathVariable("id") Integer id) {
		Order order = orderService.getById(id);
		order.setStatus(Status.CANCELED);
		orderService.save(order);
		todaysOrdersService.deleteFromTodaysOrders(order);
		return "redirect:"+req.getHeader("Referer");
	}
}
