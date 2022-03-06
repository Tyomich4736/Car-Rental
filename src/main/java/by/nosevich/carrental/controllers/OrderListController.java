package by.nosevich.carrental.controllers;

import by.nosevich.carrental.dto.OrderDto;
import by.nosevich.carrental.service.order.OrderService;
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

    public static final String ORDER_LIST_PAGE = "orderList";
    public static final String REDIRECT_FORMAT = "redirect:%s";
    public static final String TODAYS_ORDERS_LIST_PAGE = "todaysOrdersList";
    public static final String REFERER_HEADER_NAME = "Referer";
    public static final String ORDERS_ATTRIBUTE = "orders";

    private final OrderService orderService;

    @Autowired
    public OrderListController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/user/{id}")
    public String getOrdersForUser(Model model, @PathVariable("id") Integer userId) {
        List<OrderDto> orders = orderService.getAllByUser(userId);
        model.addAttribute(ORDERS_ATTRIBUTE, orders);
        return ORDER_LIST_PAGE;
    }

    @GetMapping("/cancel/{id}")
    public String cancelOrder(HttpServletRequest req, @PathVariable("id") Integer id) {
        orderService.cancelOrder(id);
        return String.format(REDIRECT_FORMAT, req.getHeader(REFERER_HEADER_NAME));
    }

    @GetMapping("/todays")
    public String getTodaysOrders(Model model) {
        List<OrderDto> orders = orderService.getTodaysOrders();
        model.addAttribute(ORDERS_ATTRIBUTE, orders);
        return TODAYS_ORDERS_LIST_PAGE;
    }

    @GetMapping("/activate/{id}")
    public String activateOrder(HttpServletRequest req, @PathVariable("id") Integer id) {
        orderService.activateOrder(id);
        return String.format(REDIRECT_FORMAT, req.getHeader(REFERER_HEADER_NAME));
    }

    @GetMapping("/finish/{id}")
    public String finishOrder(HttpServletRequest req, @PathVariable("id") Integer id) {
        orderService.finishOrder(id);
        return String.format(REDIRECT_FORMAT, req.getHeader(REFERER_HEADER_NAME));
    }
}
