package by.nosevich.carrental.controllers.clientcontrollers;

import by.nosevich.carrental.dto.UserDto;
import by.nosevich.carrental.exceptions.IncorrectUserDataException;
import by.nosevich.carrental.exceptions.UserWithSameEmailAlreadyExistsException;
import by.nosevich.carrental.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    public static final String REGISTRATION_PAGE = "auth/register";
    public static final String REDIRECT_ON_SUCCESSFUL_REGISTRATION_PAGE = "redirect:/successfulreg";
    public static final String REDIRECT_ON_LOGIN_PAGE = "redirect:/login";
    public static final String USER_ATTRIBUTE = "user";
    public static final String MAIL_ERROR_ATTRIBUTE = "mailError";
    public static final String PASSWORD_ERROR_ATTRIBUTE = "passwordError";
    public static final String MAIL_IS_BUSY_ERROR_ATTRIBUTE = "mailIsBusy";

    private final UserService userService;
    private final LocaleResolver localeResolver;

    @Autowired
    public AuthController(UserService userService, LocaleResolver localeResolver) {
        this.userService = userService;
        this.localeResolver = localeResolver;
    }

    @PostMapping("/register")
    public String createNewUser(UserDto user,
                                @RequestParam("confirmPassword") String passwordConfirmation,
                                Model model,
                                HttpServletRequest request) {
        try {
            userService.createNewUser(user, passwordConfirmation, localeResolver.resolveLocale(request));
        } catch(MessagingException e) {
            e.printStackTrace();
            model.addAttribute(USER_ATTRIBUTE, user);
            model.addAttribute(MAIL_ERROR_ATTRIBUTE, true);
            return REGISTRATION_PAGE;
        } catch(IncorrectUserDataException e) {
            model.addAttribute(USER_ATTRIBUTE, user);
            model.addAttribute(PASSWORD_ERROR_ATTRIBUTE, true);
            return REGISTRATION_PAGE;
        } catch(UserWithSameEmailAlreadyExistsException e) {
            model.addAttribute(USER_ATTRIBUTE, user);
            model.addAttribute(MAIL_IS_BUSY_ERROR_ATTRIBUTE, true);
            return REGISTRATION_PAGE;
        }
        return REDIRECT_ON_SUCCESSFUL_REGISTRATION_PAGE;
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return REGISTRATION_PAGE;
    }

    @GetMapping("/activate/{code}")
    public String activateUser(@PathVariable("code") String activationCode) {
        userService.activateUser(activationCode);
        return REDIRECT_ON_LOGIN_PAGE;
    }

}
