package by.nosevich.carrental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.service.CarService;
import by.nosevich.carrental.model.service.CategoryService;
import by.nosevich.carrental.model.service.ImageStoreService;

@Controller
@RequestMapping("/catalog")
public class CatalogController {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CarService carService;
	@Autowired
	private ImageStoreService imageStoreService;
	
	@GetMapping("")
	public String getCategories(Model model) {
		model.addAttribute("categories", categoryService.getAll());
		return "catalog/categories";
	}
	
	@GetMapping("/{category}")
	public String getCarsInCategory(Model model, @PathVariable("category") String categoryName,
			@RequestParam(value = "page",required = false) Integer pageNum) {
		if (pageNum==null)
			pageNum=0;
		Pageable pageable = PageRequest.of(pageNum, 5);
		List<Car> cars = carService.getByCategory(categoryService.getByName(categoryName), pageable);
		
		List<Car> nextCars = carService.getByCategory(categoryService.getByName(categoryName),
				PageRequest.of(pageNum+1, 5));
		
		if (nextCars.size()!=0)
			model.addAttribute("hasNextPage", true);
		model.addAttribute("cars", cars);
		model.addAttribute("currentCategory", categoryName);
		model.addAttribute("currentPage", pageNum);
		return "catalog/carList";
	}
	
	@GetMapping("/car/{id}")
	public String getCarPage(Model model, @PathVariable("id") String carId) {
		Car car = carService.getById(Integer.parseInt(carId));
		model.addAttribute("car", car);
		List<String> paths = imageStoreService.getCarImagePaths(car);
		model.addAttribute("images", paths);
		return "catalog/carPage";
	}
}
