package by.nosevich.carrental.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.orderenums.Status;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	List<Order> findAllByCarAndStatus(Car car, Status status);
}
