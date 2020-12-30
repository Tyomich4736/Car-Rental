package by.nosevich.carrental.controllers;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.userenums.Role;
import by.nosevich.carrental.model.service.CarService;
import by.nosevich.carrental.model.service.ImageStoreService;
import by.nosevich.carrental.model.service.MailService;
import by.nosevich.carrental.model.service.UserService;

@Controller
public class AuthController {

	@Autowired
	private MailService emailService;
	@Autowired
	private UserService userService;
	@Autowired
	private ImageStoreService storeService;
	@Autowired
	private CarService carService;
	
	@PostMapping("/register")
	public String createNewUser(User user, @Param("confirmPassword") String confirmPassword) throws IOException {
		try {
			if (userDataIsCorrect(user, confirmPassword)) {
				user.setActivationCode(UUID.randomUUID().toString());
				user.setRole(Role.CLIENT);
				user.setActive(false);
				emailService.sendActivationMessage(user);
				userService.saveProtectedUser(user);
			} else
				return "redirect:/register";
		} catch (Exception e) {
			return "redirect:/register";
		}
		return "redirect:/successfulreg";
	}

	@GetMapping("/register")
	public String getRegistrationPage() throws IOException {
		return "auth/register";
	}
	
	@GetMapping("/activate/{code}")
	public String activateUser(@PathVariable("code") String activationCode) {
		User user = userService.getByActivationCode(activationCode);
		user.setActive(true);
		userService.save(user);
		return "redirect:/login";
	}
	
	@PostMapping("/cars/{id}/img")
	public String handleFileUpload(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file)
			throws IOException {
		storeService.storeCarImage(carService.getById(id), file);
		return "redirect:/";
	}
	
	@GetMapping("/cars")
	public String getCarList(Model model)
			throws IOException {
		model.addAttribute("cars", carService.getAll());
		return "carList";
	}

	private boolean userDataIsCorrect(User user, String confirmPassword) {
		if (!user.getEmail().trim().equals("") 
				&& !user.getFirstName().trim().equals("")
				&& !user.getLastName().trim().equals("") 
				&& !user.getPhoneNumber().trim().equals("")
				&& user.getPassword().equals(confirmPassword))
			return true;
		else {
			return false;
		}
	}

}
