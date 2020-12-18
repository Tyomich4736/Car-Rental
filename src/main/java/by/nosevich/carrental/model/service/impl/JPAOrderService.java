package by.nosevich.carrental.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.repo.OrderRepository;
import by.nosevich.carrental.model.service.OrderService;

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

}
