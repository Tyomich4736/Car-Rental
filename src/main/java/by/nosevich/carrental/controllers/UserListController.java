package by.nosevich.carrental.controllers;

import by.nosevich.carrental.dto.UserDto;
import by.nosevich.carrental.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public static final String SEARCH_ATTRIBUTE = "search";
    public static final String HAS_NEXT_PAGE_ATTRIBUTE = "hasNextPage";
    public static final String CURRENT_PAGE_ATTRIBUTE = "currentPage";
    public static final String USERS_ATTRIBUTE = "users";
    public static final String REFERER_HEADER = "Referer";
    public static final String REDIRECT_FORMAT = "redirect:%s";
    public static final String USER_LIST_PAGE = "users/userList";
    @Autowired
    private UserService userService;

    @GetMapping
    public String getList(Model model,
                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "search", required = false) String search) {
        List<UserDto> users = userService.findBySubstring(search, pageNum);
        boolean hasNextPage = userService.hasNextSearchPage(search, pageNum);
        model.addAttribute(SEARCH_ATTRIBUTE, search);
        model.addAttribute(HAS_NEXT_PAGE_ATTRIBUTE, hasNextPage);
        model.addAttribute(CURRENT_PAGE_ATTRIBUTE, pageNum);
        model.addAttribute(USERS_ATTRIBUTE, users);
        return USER_LIST_PAGE;
    }

    @GetMapping("/set/employee")
    public String makeAnEmployee(HttpServletRequest request, @RequestParam("id") Integer userId) {
        userService.makeAnEmployee(userId);
        return String.format(REDIRECT_FORMAT, request.getHeader(REFERER_HEADER));
    }

    @GetMapping("/set/client")
    public String makeAnClient(HttpServletRequest request, @RequestParam("id") Integer userId) {
        userService.makeAClient(userId);
        return String.format(REDIRECT_FORMAT, request.getHeader(REFERER_HEADER));
    }

}
