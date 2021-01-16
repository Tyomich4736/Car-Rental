package by.nosevich.carrental.controllers.clientcontrollers;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.nosevich.carrental.model.entities.Accessory;
import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.orderenums.Status;
import by.nosevich.carrental.model.service.MailService;
import by.nosevich.carrental.model.service.entityservice.AccessoryService;
import by.nosevich.carrental.model.service.entityservice.CarService;
import by.nosevich.carrental.model.service.entityservice.OrderService;
import by.nosevich.carrental.model.service.entityservice.UserService;

@Controller
@RequestMapping("/order")
public class OrdersController {

	@Autowired
	private CarService carService;
	@Autowired
	private MailService mailService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AccessoryService accessoryService;

	@GetMapping("/{carId}")
	public String getOrderingPage(Model model, @PathVariable("carId") Integer carId) {
		Car currentCar = carService.getById(carId);
		if (currentCar == null)
			return "redirect:/catalog";
		model.addAttribute("currentCar", currentCar);

		List<Order> orders = orderService.getAllByCar(currentCar);
		model.addAttribute("busyOrders", orders);

		List<Accessory> accessories = accessoryService.getAll();
		model.addAttribute("accessories", accessories);
		return "ordering/ordering";
	}

	@PostMapping("/confirmOrder/{carId}")
	public String getConfirmPage(@RequestParam(required = false, name = "accessories") List<String> accessoryNames,
			@RequestParam("beginDate") String beginDateStr, @RequestParam("endDate") String endDateStr,
			@PathVariable("carId") Integer carId, Principal principal, Model model, HttpSession session) {

		// init and check values
		Car car = carService.getById(carId);
		Date beginDate, endDate;
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			return "redirect:/catalog";
		}
		if (car == null)
			return "redirect:/catalog";
		if (orderIsCross(beginDate, endDate, car)) {
			return "redirect:/order/"+carId;
		}
		HashSet<Accessory> accessories = new HashSet<Accessory>();
		if (accessoryNames != null)
			accessoryNames.forEach(name -> {
				Accessory currentAccessory = accessoryService.getByName(name);
				if (currentAccessory != null)
					accessories.add(currentAccessory);
			});
		
		// creating order
		Order order = new Order();
		order.setBeginDate(beginDate);
		order.setEndDate(endDate);
		order.setCar(car);
		//order.setAccessories(accessories);
		order.setStatus(Status.WAITING);
		User currentUser = userService.getByEmail(principal.getName()).get();
		order.setUser(currentUser);
		orderService.calculateAndSetPrice(order, accessories);
		
		session.setAttribute("currentOrder", order);
		session.setAttribute("currentAccessories", accessories);
		model.addAttribute("order", order);
		return "ordering/confirmOrder";
	}

	private boolean orderIsCross(Date beginDate, Date endDate, Car car) {
		List<Order> orders = orderService.getAllByCar(car);
		for (Order order : orders) {
			if ((beginDate.compareTo(order.getBeginDate()) > 0 && beginDate.compareTo(order.getEndDate()) < 0)
					|| ((endDate.compareTo(order.getBeginDate()) > 0 && endDate.compareTo(order.getEndDate()) < 0)))
				return true;
		}
		return false;
	}
	
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session, Principal principal) {
		Order order = (Order) session.getAttribute("currentOrder");
		if (order!=null) {
			orderService.save(order);
			Set<Accessory> accessories = (Set<Accessory>) session.getAttribute("currentAccessories");
			addAccessoriesToOrder(order, accessories);
//			or (accessories are not saved)
//			order.setAccessories(accessories); 
			User currentUser = userService.getByEmail(principal.getName()).get();
			try {
				mailService.sendSuccessfulOrderingMessage(currentUser, order);
			} catch (MessagingException e) {
				return "redirect:/order/myOrders";
			}
			orderService.save(order);
		}
		return "redirect:/order/myOrders";
	}

	private void addAccessoriesToOrder(Order order, Set<Accessory> accessories) {
		for(Accessory acc : accessories) {
			accessoryService.save(acc);
			order.getAccessories().add(acc);
		}
	}
	
	@GetMapping("/myOrders")
	public String getMyOrders(Model model, Principal principal) {
		User currentUser = userService.getByEmail(principal.getName()).get();
		List<Order> orders = orderService.getAllByUser(currentUser);
		model.addAttribute("orders", orders);
		return "ordering/myOrdersList";
	}
}
