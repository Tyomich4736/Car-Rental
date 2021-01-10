package by.nosevich.carrental.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.userenums.Role;
import by.nosevich.carrental.model.service.entityservice.UserService;

@Controller
@RequestMapping("/users")
public class UserListController {
	@Autowired
	private UserService userService;
	
	private static final int PAGE_SIZE = 20;
	
	@GetMapping("")
	public String getList(Model model, 
			@RequestParam(value = "page",required = false) Integer pageNum,
			@RequestParam(value = "search",required = false) String search) {
		
		if (pageNum==null)
			pageNum = 0;
		List<User> users, nextUsers;
		Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
		if (search==null) {
			users = userService.getAll(pageable);
			nextUsers = userService.getAll(PageRequest.of(pageNum+1, PAGE_SIZE));
 		} else {
 			users = userService.searchLike(search, pageable);
 			nextUsers = userService.searchLike(search, PageRequest.of(pageNum+1, PAGE_SIZE));
 			model.addAttribute("search", search);
 		}
		
		if (nextUsers.size()!=0)
			model.addAttribute("hasNextPage", true);		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("users", users);
		return "users/userList";
	}
	
	@GetMapping("/employee")
	public String makeAnEmployee(HttpServletRequest request,
			@RequestParam("id") Integer userId) {
		try {
			User user = userService.getById(userId);
			user.setRole(Role.EMPLOYEE);
			userService.save(user);
		} catch (Exception e) {
			return "redirect:/users";
		}
		String referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}
	
}
