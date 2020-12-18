package by.nosevich.carrental.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.service.CarImageStoreService;
import by.nosevich.carrental.model.service.CarService;

@Controller
public class MainController {
	
	@Autowired
	private CarImageStoreService imgService;
	@Autowired
	private CarService carService;
	
	@PostMapping("/cars/{id}/img")
	public String handleFileUpload(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file)
			throws IOException {
		imgService.store(carService.getById(id), file);
		return "redirect:/cars";
	}
	
	@GetMapping("/cars")
	public String getAllUsers(Model model) {
		carService.save(new Car());
		List<Car> cars = carService.getAll(); 
		model.addAttribute("cars", cars);

		return "carList";
	}
}
