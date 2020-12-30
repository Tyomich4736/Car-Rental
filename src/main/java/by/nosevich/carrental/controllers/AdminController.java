package by.nosevich.carrental.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Category;
import by.nosevich.carrental.model.service.CategoryService;
import by.nosevich.carrental.model.service.ImageStoreService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ImageStoreService ImageStoreService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/addCategory")
	public String addCategoryForm() {
		return "forAdmin/addCategory";
	}

	@PostMapping("/addCategory")
	public String addCategory(@Param("name") String name, @Param("file") MultipartFile file) {
		try {
			if (isImageFile(file)) {
				ImageStoreService.storeCategoryImage(file);
				Category category = new Category();
				category.setName(name);
				category.setImageName(file.getOriginalFilename());
				categoryService.save(category);
			}
		} catch (IOException e) {
			return "catalog/categories";
		}
		return "catalog/categories";
	}

	private boolean isImageFile(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		return fileName.endsWith(".png") || fileName.endsWith(".jpg") ? true : false;
	}
}
