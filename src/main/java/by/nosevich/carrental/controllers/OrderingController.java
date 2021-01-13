package by.nosevich.carrental.controllers;

import java.util.Date;
import java.util.List;

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
import by.nosevich.carrental.model.service.entityservice.AccessoryService;
import by.nosevich.carrental.model.service.entityservice.CarService;
import by.nosevich.carrental.model.service.entityservice.OrderService;

@Controller
@RequestMapping("/order")
public class OrderingController {
	
	@Autowired
	private CarService carService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AccessoryService accessoryService;
	
	@GetMapping("/{carId}")
	public String getOrderingPage(Model model, @PathVariable("carId") Integer carId) {
		Car currentCar = carService.getById(carId);
		if (currentCar==null)
			return "redirect:/catalog";
		model.addAttribute("currentCar", currentCar);
		
		List<Order> orders = orderService.getAllByCar(currentCar);
		model.addAttribute("busyOrders", orders);
		
		List<Accessory> accessories = accessoryService.getAll();
		model.addAttribute("accessories", accessories);
		return "order/ordering";
	}
	
	@PostMapping("/confirmOrder")
	public String getConfirmPage(@RequestParam(name="accessories") List<String> accesoryNames,
			@RequestParam("beginDate") Date beginDate,
			@RequestParam("endDate") Date endDate) {
		Order order = new Order();
		return null;//TODO
	}
}
