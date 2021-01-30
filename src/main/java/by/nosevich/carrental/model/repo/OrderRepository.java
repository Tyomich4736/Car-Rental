package by.nosevich.carrental.model.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Order;
import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.entities.orderenums.Status;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	List<Order> findAllByCarAndStatus(Car car, Status status);
	List<Order> findAllByUserOrderByBeginDateDesc(User user);
	List<Order> findAllByBeginDateAndStatus(Date beginDate, Status status);
	List<Order> findAllByEndDateAndStatus(Date beginDate, Status status);
	@Query("SELECT o FROM Order o WHERE o.user.id = ?1")
	List<Order> findAllByUserId(Integer id);
}
