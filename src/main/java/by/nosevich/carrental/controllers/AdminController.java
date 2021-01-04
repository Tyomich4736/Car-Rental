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
	
	@GetMapping("/{category}/addCar")
	public String addCarForm(@PathVariable("category") String category, Model model) {
		model.addAttribute("currentCategory", category);
		return "forAdmin/addCar";
	}
	
	@PostMapping("/{currentCategory}/addCar")
	public String addCar(@PathVariable("currentCategory") String currentCategory,
			
			@Param("name") String name,
			@Param("year") Integer year,
			@Param("tranmission") Transmission tranmission,
			@Param("fuelConsumption") Double fuelConsumption,
			@Param("fuelType") FuelType fuelType,
			@Param("numberOfSeats") Integer numberOfSeats,
			@Param("averageSpeed") Double averageSpeed,
			@Param("priceFrom1To3Days") Double priceFrom1To3Days,
			@Param("priceFrom4To7Days") Double priceFrom4To7Days,
			@Param("priceFrom8To15Days") Double priceFrom8To15Days,
			@Param("priceFrom16To30Days") Double priceFrom16To30Days,
			
			@RequestParam("file") MultipartFile preview) {
		Car car = new Car(null, name, year, tranmission, fuelConsumption, null, fuelType,
				numberOfSeats, averageSpeed, priceFrom1To3Days, priceFrom4To7Days, priceFrom8To15Days,
				priceFrom16To30Days, null, null, null);
		
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

	@GetMapping("/{id}/edit")
	public String editCarForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("car", carService.getById(Integer.parseInt(id)));
		return "forAdmin/editCar";
	}
	
	@PostMapping("/{id}/edit")
	public String editCar(@PathVariable("id") String id, Model model,
			@Param("name") String name,
			@Param("year") Integer year,
			@Param("tranmission") Transmission tranmission,
			@Param("fuelConsumption") Double fuelConsumption,
			@Param("fuelType") FuelType fuelType,
			@Param("numberOfSeats") Integer numberOfSeats,
			@Param("averageSpeed") Double averageSpeed,
			@Param("priceFrom1To3Days") Double priceFrom1To3Days,
			@Param("priceFrom4To7Days") Double priceFrom4To7Days,
			@Param("priceFrom8To15Days") Double priceFrom8To15Days,
			@Param("priceFrom16To30Days") Double priceFrom16To30Days) throws IOException {
		Car car = carService.getById(Integer.parseInt(id));
		
		if (name!="") car.setName(name);
		car.setYear(year);
		if (tranmission!=null) car.setTranmission(tranmission);
		car.setFuelConsumption(fuelConsumption);
		if (fuelType!=null) car.setFuelType(fuelType);
		car.setNumberOfSeats(numberOfSeats);
		car.setAverageSpeed(averageSpeed);
		if (priceFrom1To3Days!=null) car.setPriceFrom1To3Days(priceFrom1To3Days);
		if (priceFrom4To7Days!=null) car.setPriceFrom4To7Days(priceFrom4To7Days);
		if (priceFrom8To15Days!=null) car.setPriceFrom8To15Days(priceFrom8To15Days);
		if (priceFrom16To30Days!=null) car.setPriceFrom16To30Days(priceFrom16To30Days);
		carService.save(car);
		return "redirect:/catalog";
	}
	
	@GetMapping("/{id}/{imageName}/delete")
	public String deleteCarImage(@PathVariable("id") String id, Model model,
			@PathVariable("imageName") String imageName) throws NumberFormatException, IOException {
		imageStoreService.deleteCarImageFile(carService.getById(Integer.parseInt(id)), imageName);
		return "redirect:/catalog/car/"+id;
	}
	
	@GetMapping("/{id}/addImage")
	public String addCarImageForm(@PathVariable("id") String id, Model model){
		model.addAttribute("car", carService.getById(Integer.parseInt(id)));
		return "forAdmin/addImage";
	}
	
	@PostMapping("/{id}/addImage")
	public String addCarImage(@PathVariable("id") String id, Model model,
			@RequestParam("file") MultipartFile image) throws IOException{
		Car car = carService.getById(Integer.parseInt(id));
		imageStoreService.storeCarImage(car, image);
		return "redirect:/catalog/car/"+id;
	}
	
	@GetMapping("/{id}/delete")
	public String deleteCar(@PathVariable("id") String id) throws IOException{
		Car car = carService.getById(Integer.parseInt(id));
		imageStoreService.deleteAllImagesForCar(car);
		carService.delete(car);
		return "redirect:/catalog";
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
}
