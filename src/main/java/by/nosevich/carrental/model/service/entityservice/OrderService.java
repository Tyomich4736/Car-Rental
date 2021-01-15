package by.nosevich.carrental.model.service.entityservice;

import java.util.List;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;

public interface OrderService extends DAO<Order>{
	List<Order> getAllByCar(Car car);
	void calculateAndSetPrice(Order order);
	List<Order> getAllByUser(User user);
}
