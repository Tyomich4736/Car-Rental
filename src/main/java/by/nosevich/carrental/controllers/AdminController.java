package by.nosevich.carrental.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Category;
import by.nosevich.carrental.model.service.CarService;
import by.nosevich.carrental.model.service.CategoryService;
import by.nosevich.carrental.model.service.ImageStoreService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ImageStoreService imageStoreService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CarService carService;

	@GetMapping("/addCategory")
	public String addCategoryForm() {
		return "forAdmin/addCategory";
	}

	@PostMapping("/addCategory")
	public String addCategory(@Param("name") String name, @Param("file") MultipartFile file) {
		try {
			if (isImageFile(file) && !hasCategoryWithSameName(name)) {
				imageStoreService.storeCategoryImage(file);
				Category category = new Category();
				category.setName(name);
				category.setImageName(file.getOriginalFilename());
				categoryService.save(category);
			}
		} catch (IOException e) {
			return "redirect:/catalog";
		}
		return "redirect:/catalog";
	}
	
	@GetMapping("/{category}/addCar")
	public String addCarForm(@PathVariable("category") String category, Model model) {
		model.addAttribute("currentCategory", category);
		return "forAdmin/addCar";
	}
	
	@PostMapping("/{currentCategory}/addCar")
	public String addCar(@PathVariable("currentCategory") String currentCategory,
			Car car,
			@RequestParam("file") MultipartFile preview) {
		try {
			if (isImageFile(preview)) {
				carService.save(car);
				imageStoreService.storeCarPreview(car, preview);
				car.setCategory(categoryService.getByName(currentCategory));
				carService.save(car);
			} else 
				return "redirect:/admin/"+currentCategory+"/addCar";
		} catch(Exception e) {
			carService.delete(car);
			return "redirect:/admin/"+currentCategory+"/addCar";
		}
		return "redirect:/catalog/"+currentCategory;
	}

	private boolean isImageFile(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		return fileName.endsWith(".png") || fileName.endsWith(".jpg") ? true : false;
	}
	private boolean hasCategoryWithSameName(String name) {
		for(Category category : categoryService.getAll())
			if (category.getImageName().equals(name)) return true;
		return false;
	}
}
