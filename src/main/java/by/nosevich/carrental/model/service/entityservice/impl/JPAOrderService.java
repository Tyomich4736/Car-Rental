package by.nosevich.carrental.model.service.entityservice.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.nosevich.carrental.model.entities.Accessory;
import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.orderenums.Status;
import by.nosevich.carrental.model.repo.OrderRepository;
import by.nosevich.carrental.model.service.entityservice.OrderService;

@Service
@Transactional
public class JPAOrderService implements OrderService{

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
		List<Order> result = repo.findAllByCarAndStatus(car, Status.ACTIVE);
		result.addAll(repo.findAllByCarAndStatus(car, Status.WAITING));
		return result;
	}

	@Override
	public void calculateAndSetPrice(Order order) {
		double price = 0;
		int days = Period.between(convertDateToLocalDate(order.getBeginDate()), 
								convertDateToLocalDate(order.getEndDate())).getDays();
		if (days<=3) {
			price+=order.getCar().getPriceFrom1To3Days();
		} else if (days<=7) {
			price+=order.getCar().getPriceFrom4To7Days();
		} else if (days<=15) {
			price+=order.getCar().getPriceFrom8To15Days();
		} else if (days<=30) {
			price+=order.getCar().getPriceFrom16To30Days();
		}
		for(Accessory acc : order.getAccessories()) {
			price+=acc.getPrice();
		}
		order.setPrice(price);
	}

	private LocalDate convertDateToLocalDate(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}

	@Override
	public List<Order> getAllByUser(User user) {
		return repo.findAllByUserOrderByBeginDateDesc(user);
	}

	@Override
	public List<Order> getAllByBeginDateAndStatus(Date beginDate, Status status) {
		return repo.findAllByBeginDateAndStatus(beginDate, status);
	}

	@Override
	public List<Order> getAllByEndDateAndStatus(Date endDate, Status status) {
		return repo.findAllByEndDateAndStatus(endDate, status);
	}

	@Override
	public Order getByStatusAndUser(Status status, User user) {
		return repo.findByStatusAndUser(status, user);
	}
}
