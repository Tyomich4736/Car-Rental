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
import by.nosevich.carrental.model.service.ImageStoreService;
import by.nosevich.carrental.model.service.entityservice.CarService;
import by.nosevich.carrental.model.service.entityservice.CategoryService;

@Controller
@RequestMapping("/admin")
public class AdminCarController {

	@Autowired
	private ImageStoreService imageStoreService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CarService carService;
	
	@GetMapping("/{category}/addCar")
	public String addCarForm(@PathVariable("category") String category, Model model) {
		model.addAttribute("currentCategory", category);
		return "forAdmin/addCar";
	}

	@PostMapping("/{currentCategory}/addCar")
	public String addCar(@PathVariable("currentCategory") String currentCategory, Car car,
			@RequestParam("file") MultipartFile preview) {
		try {
			carService.save(car);
			imageStoreService.storeCarPreview(car, preview);
			car.setCategory(categoryService.getByName(currentCategory));
			carService.save(car);
		} catch (Exception e) {
			carService.delete(car);
			return "redirect:/admin/" + currentCategory + "/addCar";
		}
		return "redirect:/catalog/" + currentCategory;
	}
	
	@GetMapping("/{carId}/edit")
	public String editCarForm(@PathVariable("carId") String id, Model model) {
		model.addAttribute("car", carService.getById(Integer.parseInt(id)));
		return "forAdmin/editCar";
	}

	@PostMapping("/{carId}/edit")
	public String editCar(@PathVariable("carId") String id, Model model, Car editedCar) {
		Car car = carService.getById(Integer.parseInt(id));
		setNotNullFieldsFromCarToCar(editedCar, car);
		carService.save(car);
		return "redirect:/catalog";
	}

	private void setNotNullFieldsFromCarToCar(Car from, Car to) {
		if (from.getName() != "")
			to.setName(from.getName());
		to.setYear(from.getYear());
		if (from.getTranmission() != null)
			to.setTranmission(from.getTranmission());
		to.setFuelConsumption(from.getFuelConsumption());
		if (from.getFuelType() != null)
			to.setFuelType(from.getFuelType());
		to.setNumberOfSeats(from.getNumberOfSeats());
		to.setMaxSpeed(from.getMaxSpeed());
		if (from.getPriceFrom1To3Days() != null)
			to.setPriceFrom1To3Days(from.getPriceFrom1To3Days());
		if (from.getPriceFrom4To7Days() != null)
			to.setPriceFrom4To7Days(from.getPriceFrom4To7Days());
		if (from.getPriceFrom8To15Days() != null)
			to.setPriceFrom8To15Days(from.getPriceFrom8To15Days());
		if (from.getPriceFrom16To30Days() != null)
			to.setPriceFrom16To30Days(from.getPriceFrom16To30Days());
	}

	@GetMapping("/{carId}/{imageName}/delete")
	public String deleteCarImage(@PathVariable("carId") String id, Model model,
			@PathVariable("imageName") String imageName) throws NumberFormatException, IOException {
		imageStoreService.deleteCarImageFile(carService.getById(Integer.parseInt(id)), imageName);
		return "redirect:/catalog/car/" + id;
	}

	@GetMapping("/{carId}/addImage")
	public String addCarImageForm(@PathVariable("carId") String id, Model model) {
		model.addAttribute("car", carService.getById(Integer.parseInt(id)));
		return "forAdmin/addImage";
	}

	@PostMapping("/{carId}/addImage")
	public String addCarImage(@PathVariable("carId") String id, Model model,
			@RequestParam("file") MultipartFile image) {
		Car car = carService.getById(Integer.parseInt(id));
		try {
			imageStoreService.storeCarImage(car, image);
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/admin/" + id + "/addImage";
		}
		return "redirect:/catalog/car/" + id;
	}

	@GetMapping("/{carId}/delete")
	public String deleteCar(@PathVariable("carId") String id) throws IOException {
		Car car = carService.getById(Integer.parseInt(id));
		imageStoreService.deleteAllImagesForCar(car);
		carService.delete(car);
		return "redirect:/catalog";
	}
}
