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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Category;
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
	
	private static final int PAGE_SIZE = 5;
	
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
		Category currentCategory = categoryService.getByName(categoryName);
		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		List<Car> cars = carService.getByCategory(currentCategory, pageable);
		List<Car> nextCars = carService.getByCategory(currentCategory, PageRequest.of(pageNum+1, PAGE_SIZE));
		
		if (nextCars.size()!=0)
			model.addAttribute("hasNextPage", true);
		model.addAttribute("cars", cars);
		model.addAttribute("currentCategory", categoryName);
		model.addAttribute("currentPage", pageNum);
		return "catalog/carList";
	}
	
	@RequestMapping(value = "/search", method = {RequestMethod.POST, RequestMethod.GET})
	public String getCarsBySeach(Model model,
			@RequestParam(value = "page",required = false) Integer pageNum,
			@RequestParam(value = "search") String searchRequest) {
		//TODO validation
		if (pageNum==null)
			pageNum=0;
		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		List<Car> cars = carService.searchByNameLike(searchRequest, pageable);
		List<Car> nextCars = carService.searchByNameLike(searchRequest, PageRequest.of(pageNum+1, PAGE_SIZE));
		
		if (nextCars.size()!=0)
			model.addAttribute("hasNextPage", true);
		model.addAttribute("cars", cars);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("search", searchRequest);
		return "catalog/searchResults";
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
