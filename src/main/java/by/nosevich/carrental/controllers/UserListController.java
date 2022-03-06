package by.nosevich.carrental.controllers;

import by.nosevich.carrental.dto.UserDto;
import by.nosevich.carrental.model.User;
import by.nosevich.carrental.model.enums.UserRole;
import by.nosevich.carrental.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserListController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String getList(Model model,
                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "search", required = false) String search) {
        List<UserDto> users = userService.findBySubstring(search, pageNum);
        boolean hasNextPage = userService.hasNextSearchPage(search, pageNum);
        model.addAttribute("search", search);
        model.addAttribute("hasNextPage", hasNextPage);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("users", users);
        return "users/userList";
    }

    @GetMapping("/set/employee")
    public String makeAnEmployee(HttpServletRequest request, @RequestParam("id") Integer userId) {
        userService.makeAnEmployee(userId);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/set/client")
    public String makeAnClient(HttpServletRequest request, @RequestParam("id") Integer userId) {
        userService.makeAnClient(userId);
        return "redirect:" + request.getHeader("Referer");
    }

}
