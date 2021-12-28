package by.nosevich.carrental.service.order.current;

import by.nosevich.carrental.entities.Order;
import by.nosevich.carrental.entities.orderenums.Status;
import by.nosevich.carrental.service.mailsender.MailService;
import by.nosevich.carrental.service.order.control.OrdersControlService;
import by.nosevich.carrental.service.order.current.CurrentOrdersService;
import by.nosevich.carrental.service.order.OrderService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CurrentOrdersServiceImpl implements CurrentOrdersService, InitializingBean {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MailService mailService;
    @Autowired
    private OrdersControlService ordersControlService;

    private List<Order> todaysOrders = new ArrayList<Order>();

    public List<Order> getTodaysOrders() {
        return todaysOrders;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?", zone = "Europe/Minsk")
    public void updateTodaysOrders() {
        cancelTodaysWaitingOrders();
        addNewOrdersInList();
    }

    private void cancelTodaysWaitingOrders() {
        for(Order order : todaysOrders) {
            if(order.getStatus() == Status.WAITING) {
                ordersControlService.cancelOrder(order);
            }
        }
    }

    private void addNewOrdersInList() {
        Date today = new Date();
        for(Order order : orderService.getAllByBeginDateAndStatus(today, Status.WAITING)) {
            addToTodaysOrders(order);
        }
        todaysOrders.addAll(orderService.getAllByEndDateAndStatus(today, Status.ACTIVE));
    }

    @Override
    public void addToTodaysOrders(Order order) {
        todaysOrders.add(order);
        try {
            mailService.sendPickUpOrderMessage(order.getUser(), order);
        } catch(MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Order order) {
        for(int i = 0; i < todaysOrders.size(); i++) {
            Order o = todaysOrders.get(i);
            if(order.getId() == o.getId()) {
                todaysOrders.remove(i);
            }
        }
        todaysOrders.remove(order);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        updateTodaysOrders();
    }
}
