package by.nosevich.carrental.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import by.nosevich.carrental.model.service.CarImageStoreService;
import by.nosevich.carrental.model.service.CarService;

@RestController
public class ImagePathsController {
	
	@Autowired
	private CarImageStoreService imgService;
	@Autowired
	private CarService carService;
	
	
	@GetMapping("/cars/{id}/imagepaths")
	@ResponseBody
	public List<String> getImagePaths(@PathVariable("id") Integer id)
			throws IOException {
		List<String> paths = imgService.getImagePaths(carService.getById(id));
		return paths;
	}
}
