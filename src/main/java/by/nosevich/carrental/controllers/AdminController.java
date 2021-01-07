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
import by.nosevich.carrental.model.entities.carenums.FuelType;
import by.nosevich.carrental.model.entities.carenums.Transmission;
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
	public String addCategory(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
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
	
	private boolean hasCategoryWithSameName(String name) {
		for(Category category : categoryService.getAll())
			if (category.getImageName().equals(name)) return true;
		return false;
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
			if (isImageFile(preview) && carIsCorrectly(car)) {
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
	
	private boolean carIsCorrectly(Car car) {
		if (car.getName()!=""
				&& car.getPriceFrom1To3Days()!=null
				&& car.getPriceFrom4To7Days()!=null
				&& car.getPriceFrom8To15Days()!=null
				&& car.getPriceFrom16To30Days()!=null) {
			return true;
		}
		return false;
	}
	
	private boolean isImageFile(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		return fileName.endsWith(".png") || fileName.endsWith(".jpg") ? true : false;
	}
	
	@GetMapping("/category/{category}/delete")
	public String deleteCategory(@PathVariable("category") String categoryName) throws IOException {
		Category category = categoryService.getByName(categoryName);
		for(Car car : carService.getByCategory(category)) {
			imageStoreService.deleteAllImagesForCar(car);
			carService.delete(car);
		}
		imageStoreService.deleteCategoryImage(category);
		categoryService.delete(category);
		return "redirect:/catalog";
	}

	@GetMapping("/{carId}/edit")
	public String editCarForm(@PathVariable("carId") String id, Model model) {
		model.addAttribute("car", carService.getById(Integer.parseInt(id)));
		return "forAdmin/editCar";
	}
	
	@PostMapping("/{carId}/edit")
	public String editCar(@PathVariable("carId") String id, Model model, Car editedCar){
		Car car = carService.getById(Integer.parseInt(id));
		
		if (editedCar.getName()!="") car.setName(editedCar.getName());
		car.setYear(editedCar.getYear());
		if (editedCar.getTranmission()!=null) car.setTranmission(editedCar.getTranmission());
		car.setFuelConsumption(editedCar.getFuelConsumption());
		if (editedCar.getFuelType()!=null) car.setFuelType(editedCar.getFuelType());
		car.setNumberOfSeats(editedCar.getNumberOfSeats());
		car.setAverageSpeed(editedCar.getAverageSpeed());
		if (editedCar.getPriceFrom1To3Days()!=null) car.setPriceFrom1To3Days(editedCar.getPriceFrom1To3Days());
		if (editedCar.getPriceFrom4To7Days()!=null) car.setPriceFrom4To7Days(editedCar.getPriceFrom4To7Days());
		if (editedCar.getPriceFrom8To15Days()!=null) car.setPriceFrom8To15Days(editedCar.getPriceFrom8To15Days());
		if (editedCar.getPriceFrom16To30Days()!=null) car.setPriceFrom16To30Days(editedCar.getPriceFrom16To30Days());
		carService.save(car);
		return "redirect:/catalog";
	}
	
	@GetMapping("/{carId}/{imageName}/delete")
	public String deleteCarImage(@PathVariable("carId") String id, Model model,
			@PathVariable("imageName") String imageName) throws NumberFormatException, IOException {
		imageStoreService.deleteCarImageFile(carService.getById(Integer.parseInt(id)), imageName);
		return "redirect:/catalog/car/"+id;
	}
	
	@GetMapping("/{carId}/addImage")
	public String addCarImageForm(@PathVariable("carId") String id, Model model){
		model.addAttribute("car", carService.getById(Integer.parseInt(id)));
		return "forAdmin/addImage";
	}
	
	@PostMapping("/{carId}/addImage")
	public String addCarImage(@PathVariable("carId") String id, Model model,
			@RequestParam("file") MultipartFile image){
		Car car = carService.getById(Integer.parseInt(id));
		try {
		imageStoreService.storeCarImage(car, image);
		} catch (IOException e) {
			return "redirect:/admin/"+id+"/addImage";
		}
		return "redirect:/catalog/car/"+id;
	}
	
	@GetMapping("/{carId}/delete")
	public String deleteCar(@PathVariable("carId") String id) throws IOException{
		Car car = carService.getById(Integer.parseInt(id));
		imageStoreService.deleteAllImagesForCar(car);
		carService.delete(car);
		return "redirect:/catalog";
	}
	
}
