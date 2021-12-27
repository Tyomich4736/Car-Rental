package by.nosevich.carrental.controllers;

import by.nosevich.carrental.entities.Order;
import by.nosevich.carrental.entities.User;
import by.nosevich.carrental.service.OrdersControlService;
import by.nosevich.carrental.service.TodaysOrdersListService;
import by.nosevich.carrental.service.modelservice.OrderService;
import by.nosevich.carrental.service.modelservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/orders")
@Transactional
public class OrderListController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrdersControlService ordersControlService;
    @Autowired
    private TodaysOrdersListService todaysOrdersListService;

    @GetMapping("/user/{id}")
    public String getOrdersForUser(Model model, @PathVariable("id") Integer userId) {
        User user = userService.getById(userId);
        List<Order> orders = orderService.getAllByUser(user);
        model.addAttribute("orders", orders);
        return "orderList";
    }

    @GetMapping("/cancel/{id}")
    public String cancelOrder(HttpServletRequest req, @PathVariable("id") Integer id) {
        Order order = orderService.getById(id);
        ordersControlService.cancelOrder(order);
        return "redirect:" + req.getHeader("Referer");
    }

    @GetMapping("/todays")
    public String getTodaysOrders(Model model) {
        List<Order> orders = todaysOrdersListService.getTodaysOrders();
        model.addAttribute("orders", orders);
        return "todaysOrdersList";
    }

    @GetMapping("/activate/{id}")
    public String activateOrder(HttpServletRequest req, @PathVariable("id") Integer id) {
        Order order = orderService.getById(id);
        ordersControlService.activateOrder(order);
        return "redirect:" + req.getHeader("Referer");
    }

    @GetMapping("/finish/{id}")
    public String finishOrder(HttpServletRequest req, @PathVariable("id") Integer id) {
        Order order = orderService.getById(id);
        ordersControlService.finishOrder(order);
        return "redirect:" + req.getHeader("Referer");
    }
}
