package by.nosevich.carrental.service.order;

import by.nosevich.carrental.dto.OrderDto;
import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Order;
import by.nosevich.carrental.model.User;
import by.nosevich.carrental.model.enums.OrderStatus;
import by.nosevich.carrental.service.DaoService;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderService extends DaoService<OrderDto> {
    List<OrderDto> getAllBusyByCar(Integer carId);

    void clearUnconfirmedOrderForUser(String userEmail);

    Optional<OrderDto> saveUnconfirmedOrder(List<Integer> accessoryNames, String beginDateStr, String endDateStr,
                                            Integer carId,
                                            String username) throws ParseException;

    void confirmUnconfirmedOrder(String userEmail);

    List<OrderDto> getAllByUser(String userEmail);

    List<OrderDto> getAllByUser(Integer userId);

    void cancelOrder(Integer orderId);

    void activateOrder(Integer orderId);

    void finishOrder(Integer orderId);

    List<OrderDto> getTodaysOrders();

    void sendTodaysPickUpMessages();
}
