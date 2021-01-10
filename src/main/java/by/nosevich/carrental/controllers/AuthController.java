package by.nosevich.carrental.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.userenums.Role;
import by.nosevich.carrental.model.service.MailService;
import by.nosevich.carrental.model.service.entityservice.UserService;

@Controller
public class AuthController {

	@Autowired
	private MailService emailService;
	@Autowired
	private UserService userService;
	
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
		} catch (MessagingException e) {
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
