package by.nosevich.carrental.controllers.admincontrollers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
import by.nosevich.carrental.model.service.ImageStoreService;
import by.nosevich.carrental.model.service.entityservice.CarService;
import by.nosevich.carrental.model.service.entityservice.CategoryService;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {

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
	public String addCategory(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		try {
			if (!hasCategoryWithSameName(name)) {
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

	private boolean hasCategoryWithSameName(String name) {
		for (Category category : categoryService.getAll())
			if (category.getImageName().equals(name))
				return true;
		return false;
	}

	@GetMapping("/category/{category}/delete")
	public String deleteCategory(@PathVariable("category") String categoryName) throws IOException {
		Category category = categoryService.getByName(categoryName);
		for (Car car : carService.getByCategory(category)) {
			imageStoreService.deleteAllImagesForCar(car);
			carService.delete(car);
		}
		imageStoreService.deleteCategoryImage(category);
		categoryService.delete(category);
		return "redirect:/catalog";
	}
}
