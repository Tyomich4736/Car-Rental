package by.nosevich.carrental.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.userproperties.Role;
import by.nosevich.carrental.model.service.CarImageStoreService;
import by.nosevich.carrental.model.service.CarService;
import by.nosevich.carrental.model.service.UserService;
import by.nosevich.carrental.security.UserAuthService;

@Controller
public class RegistrationController {

	@Autowired
	private UserAuthService userAuthService;

	@PostMapping("/register")
	public String createNewUser(User user, @Param("confirmPassword") String confirmPassword) throws IOException {
		user.setRole(Role.CLIENT);
		user.setActive(false);
		if (user.getPassword().equals(confirmPassword)) {
			userAuthService.saveProtectedUser(user);
		}
		return "redirect:/login";
	}

	@GetMapping("/register")
	public String getRegistrationPage() throws IOException {
		return "auth/register";
	}

}
