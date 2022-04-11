package by.nosevich.carrental.controllers.clientcontrollers;

import by.nosevich.carrental.dto.AccessoryDto;
import by.nosevich.carrental.dto.CarDto;
import by.nosevich.carrental.dto.OrderDto;
import by.nosevich.carrental.exceptions.OrderIsCrossException;
import by.nosevich.carrental.service.accessory.AccessoryService;
import by.nosevich.carrental.service.car.CarService;
import by.nosevich.carrental.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
@Transactional
public class OrderingController {

    public static final String REDIRECT_ON_CATALOG_PAGE = "redirect:/catalog";
    public static final String REDIRECT_ON_HOME_PAGE = "redirect:/home";
    public static final String CONFIRM_ORDER_PAGE = "ordering/confirmOrder";
    public static final String REDIRECT_ON_ORDER_PAGE_WITH_CROSS = "redirect:/order/%d?cross=true";
    public static final String REDIRECT_ON_MY_ORDERS_PAGE = "redirect:/order/myOrders";
    public static final String MY_ORDERS_LIST_PAGE = "ordering/myOrdersList";
    public static final String ORDERING_PAGE = "ordering/ordering";
    public static final String CURRENT_CAR_ATTRIBUTE = "currentCar";
    public static final String BUSY_ORDERS_ATTRIBUTE = "busyOrders";
    public static final String ACCESSORIES_ATTRIBUTE = "accessories";
    public static final String ORDER_ATTRIBUTE = "order";
    public static final String ORDERS_ATTRIBUTE = "orders";

    private final CarService carService;
    private final OrderService orderService;
    private final AccessoryService accessoryService;
    private final LocaleResolver localeResolver;

    @Autowired
    public OrderingController(CarService carService,
                              OrderService orderService,
                              AccessoryService accessoryService,
                              LocaleResolver localeResolver) {
        this.carService = carService;
        this.orderService = orderService;
        this.accessoryService = accessoryService;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/{carId}")
    public String getOrderingPage(Model model, @PathVariable("carId") Integer carId, Principal principal) {
        orderService.clearUnconfirmedOrderForUser(principal.getName());

        Optional<CarDto> optionalCurrentCar = carService.getById(carId);
        if (!optionalCurrentCar.isPresent()) {
            return REDIRECT_ON_CATALOG_PAGE;
        }
        CarDto currentCar = optionalCurrentCar.get();
        model.addAttribute(CURRENT_CAR_ATTRIBUTE, currentCar);
        List<OrderDto> orders = orderService.getAllBusyByCar(currentCar.getId());
        model.addAttribute(BUSY_ORDERS_ATTRIBUTE, orders);

        List<AccessoryDto> accessories = accessoryService.getAll();
        model.addAttribute(ACCESSORIES_ATTRIBUTE, accessories);
        return ORDERING_PAGE;
    }

    @PostMapping("/confirmOrder/{carId}")
    public String getConfirmPage(@RequestParam(required = false, name = "accessories") List<Integer> accessoryIds,
                                 @RequestParam("beginDate") String beginDateStr,
                                 @RequestParam("endDate") String endDateStr,
                                 @PathVariable("carId") Integer carId,
                                 Principal principal,
                                 Model model) {
        try {
            Optional<OrderDto> optionalOrder =
                    orderService.saveUnconfirmedOrder(accessoryIds, beginDateStr, endDateStr, carId,
                                                      principal.getName());
            if (!optionalOrder.isPresent()) {
                return REDIRECT_ON_HOME_PAGE;
            }
            model.addAttribute(ORDER_ATTRIBUTE, optionalOrder.get());
            return CONFIRM_ORDER_PAGE;
        } catch(OrderIsCrossException e) {
            return String.format(REDIRECT_ON_ORDER_PAGE_WITH_CROSS, carId);
        } catch(Exception e) {
            return REDIRECT_ON_HOME_PAGE;
        }
    }

    @GetMapping("/saveOrder")
    public String saveOrder(Principal principal, HttpServletRequest request) {
        orderService.confirmUnconfirmedOrder(principal.getName(), localeResolver.resolveLocale(request));
        return REDIRECT_ON_MY_ORDERS_PAGE;
    }

    @GetMapping("/myOrders")
    public String getMyOrders(Model model, Principal principal) {
        List<OrderDto> orders = orderService.getAllByUser(principal.getName());
        model.addAttribute(ORDERS_ATTRIBUTE, orders);
        return MY_ORDERS_LIST_PAGE;
    }
}
