package by.nosevich.carrental.service.order;

import by.nosevich.carrental.model.Accessory;
import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Order;
import by.nosevich.carrental.model.User;
import by.nosevich.carrental.model.enums.OrderStatus;
import by.nosevich.carrental.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class JPAOrderService implements OrderService {

    @Autowired
    private OrderRepository repo;

    @Override
    public List<Order> getAll() {
        return repo.findAll();
    }

    @Override
    public Order getById(int id) {
        return repo.getOne(id);
    }

    @Override
    public void delete(Order entity) {
        repo.delete(entity);
    }

    @Override
    public void save(Order entity) {
        repo.save(entity);
    }

    @Override
    public List<Order> getAllByCar(Car car) {
        List<Order> result = repo.findAllByCarAndStatus(car, OrderStatus.ACTIVE);
        result.addAll(repo.findAllByCarAndStatus(car, OrderStatus.WAITING));
        return result;
    }

    @Override
    public void calculateAndSetPrice(Order order) {
        double price = 0;
        int days =
                Period.between(convertDateToLocalDate(order.getBeginDate()), convertDateToLocalDate(order.getEndDate()))
                        .getDays();
        if(days <= 3) {
            price += order.getCar().getPriceFrom1To3Days();
        } else if(days <= 7) {
            price += order.getCar().getPriceFrom4To7Days();
        } else if(days <= 15) {
            price += order.getCar().getPriceFrom8To15Days();
        } else if(days <= 30) {
            price += order.getCar().getPriceFrom16To30Days();
        }
        for(Accessory acc : order.getAccessories()) {
            price += acc.getPrice();
        }
        order.setPrice(price);
    }

    private LocalDate convertDateToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public List<Order> getAllByUser(User user) {
        return repo.findAllByUserOrderByBeginDateDesc(user);
    }

    @Override
    public List<Order> getAllByBeginDateAndStatus(Date beginDate, OrderStatus orderStatus) {
        return repo.findAllByBeginDateAndStatus(beginDate, orderStatus);
    }

    @Override
    public List<Order> getAllByEndDateAndStatus(Date endDate, OrderStatus orderStatus) {
        return repo.findAllByEndDateAndStatus(endDate, orderStatus);
    }

    @Override
    public Order getByStatusAndUser(OrderStatus orderStatus, User user) {
        for(Order order : repo.findAllByUserId(user.getId())) {
            if(order.getOrderStatus() == orderStatus) {
                return order;
            }
        }
        return null;
    }
}
