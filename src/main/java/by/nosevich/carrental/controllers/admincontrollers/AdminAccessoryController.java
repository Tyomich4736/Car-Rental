package by.nosevich.carrental.controllers.admincontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import by.nosevich.carrental.model.entities.Accessory;
import by.nosevich.carrental.model.service.entityservice.AccessoryService;

@Controller
@RequestMapping("/admin")
public class AdminAccessoryController {
	
	@Autowired
	private AccessoryService accessoryService;
	
	@GetMapping("/accessories")
	public String getAccessoryList(Model model) {
		List<Accessory> accessories = accessoryService.getAll();
		model.addAttribute("accessories", accessories);
		return "forAdmin/accessoryList";
	}
	
	@GetMapping("/addAccessory")
	public String addAccessoryPage() {
		return "forAdmin/addAccessory";
	}
	
	@PostMapping("/addAccessory")
	public String addAccessory(Accessory acc) {
		if (acc!=null) 
			accessoryService.save(acc);
		return "redirect:/admin/accessories";
	}
}
