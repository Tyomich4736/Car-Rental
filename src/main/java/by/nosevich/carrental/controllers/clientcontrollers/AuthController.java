package by.nosevich.carrental.controllers.clientcontrollers;

import by.nosevich.carrental.entities.User;
import by.nosevich.carrental.entities.userenums.Role;
import by.nosevich.carrental.service.MailService;
import by.nosevich.carrental.service.modelservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AuthController {

    @Autowired
    private MailService emailService;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String createNewUser(User user, @Param("confirmPassword") String confirmPassword, HttpServletRequest request, Model model)
    throws IOException {
        try {
            if(userDataIsCorrect(user, confirmPassword)) {
                user.setActivationCode(UUID.randomUUID().toString());
                user.setRole(Role.CLIENT);
                user.setActive(false);
                emailService.sendActivationMessage(user);
                userService.saveProtectedUser(user);
            } else {
                putUserDataInAttributes(user, model);
                model.addAttribute("passwordError", true);
                return "auth/register";
            }
        } catch(MessagingException e) {
            e.printStackTrace();
            putUserDataInAttributes(user, model);
            model.addAttribute("mailError", true);
            return "auth/register";
        }
        return "redirect:/successfulreg";
    }

    private void putUserDataInAttributes(User user, Model model) {
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("phone", user.getPhoneNumber());
        model.addAttribute("email", user.getEmail());
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
        if(!user.getEmail().trim().equals("") && !user.getFirstName().trim().equals("") &&
                !user.getLastName().trim().equals("") && !user.getPhoneNumber().trim().equals("") &&
                user.getPassword().equals(confirmPassword)) return true;
        else {
            return false;
        }
    }

}
