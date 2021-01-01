package by.nosevich.carrental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.service.CarService;
import by.nosevich.carrental.model.service.CategoryService;

@Controller
@RequestMapping("/catalog")
public class CatalogController {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CarService carService;
	
	@GetMapping("")
	public String getCategories(Model model) {
		model.addAttribute("categories", categoryService.getAll());
		return "catalog/categories";
	}
	
	@GetMapping("/{category}")
	public String getCarsInCategory(Model model, @PathVariable("category") String categoryName) {
		List<Car> cars = carService.getByCategory(categoryService.getByName(categoryName)); 
		model.addAttribute("cars", cars);
		model.addAttribute("currentCategory", categoryName);
		return "catalog/carsInCategory";
	}
}
