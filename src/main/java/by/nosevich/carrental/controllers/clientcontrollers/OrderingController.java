package by.nosevich.carrental.controllers.clientcontrollers;

import java.security.Principal;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.nosevich.carrental.exceptions.OrderIsCrossException;
import by.nosevich.carrental.model.entities.Accessory;
import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.orderenums.Status;
import by.nosevich.carrental.model.service.OrdersControlService;
import by.nosevich.carrental.model.service.entityservice.AccessoryService;
import by.nosevich.carrental.model.service.entityservice.CarService;
import by.nosevich.carrental.model.service.entityservice.OrderService;
import by.nosevich.carrental.model.service.entityservice.UserService;

@Controller
@RequestMapping("/order")
@Transactional
public class OrderingController {

	@Autowired
	private CarService carService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AccessoryService accessoryService;
	@Autowired
	private OrdersControlService ordersControlService;

	@GetMapping("/{carId}")
	public String getOrderingPage(Model model, @PathVariable("carId") Integer carId, Principal principal) {
		Order previousUnconfirmedOrder = orderService.getByStatusAndUser(Status.UNCOMFIRMED, userService.getByEmail(principal.getName()));
		if (previousUnconfirmedOrder!=null) {
			orderService.delete(previousUnconfirmedOrder);
		}
		
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
			@PathVariable("carId") Integer carId, Principal principal, Model model) {
		
		try {
			Order order = ordersControlService.saveUnconfirmedOrder(accessoryNames, beginDateStr,
					endDateStr, carId, principal.getName());
			model.addAttribute("order", order);
		} catch (OrderIsCrossException e) {
			return "redirect:/order/"+carId+"?cross=true";
		} catch (Exception e) {
			return "redirect:/home";
		}
		return "ordering/confirmOrder";
	}
	
	@GetMapping("/saveOrder")
	public String saveOrder(Principal principal) {
		User currentUser = userService.getByEmail(principal.getName());
		Order order = orderService.getByStatusAndUser(Status.UNCOMFIRMED, currentUser);
		if (order!=null) {
			try {
				ordersControlService.waitOrder(order);
			} catch (MessagingException e) {
				orderService.delete(order); 
				return "redirect:/order/myOrders";
			}
		}
		return "redirect:/order/myOrders";
	}
	
	@GetMapping("/myOrders")
	public String getMyOrders(Model model, Principal principal) {
		User currentUser = userService.getByEmail(principal.getName());
		List<Order> orders = orderService.getAllByUser(currentUser);
		model.addAttribute("orders", orders);
		return "ordering/myOrdersList";
	}
}
