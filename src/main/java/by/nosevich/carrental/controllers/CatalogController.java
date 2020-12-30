package by.nosevich.carrental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/catalog")
public class CatalogController {
	
	@GetMapping("/")
	public String getCategories() {
		return "catalog/categories";
	}
	
}
