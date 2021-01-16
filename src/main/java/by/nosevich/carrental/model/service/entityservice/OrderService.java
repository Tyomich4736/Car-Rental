package by.nosevich.carrental.model.service.entityservice;

import java.util.List;
import java.util.Set;

import by.nosevich.carrental.model.entities.Accessory;
import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;

public interface OrderService extends DAO<Order>{
	List<Order> getAllByCar(Car car);
	List<Order> getAllByUser(User user);
	void calculateAndSetPrice(Order order, Set<Accessory> accessories);
}
