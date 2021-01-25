package by.nosevich.carrental.model.service.entityservice;

import java.util.Date;
import java.util.List;
import java.util.Set;

import by.nosevich.carrental.model.entities.Accessory;
import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.orderenums.Status;

public interface OrderService extends DAO<Order>{
	List<Order> getAllByCar(Car car);
	List<Order> getAllByUser(User user);
	void calculateAndSetPrice(Order order);
	
	List<Order> getAllByBeginDateAndStatus(Date beginDate, Status status);
	List<Order> getAllByEndDateAndStatus(Date endDate, Status status);
	Order getByStatusAndUser(Status status, User user);
}
