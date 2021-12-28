package by.nosevich.carrental.repository;

import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Order;
import by.nosevich.carrental.model.User;
import by.nosevich.carrental.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByCarAndStatus(Car car, OrderStatus orderStatus);

    List<Order> findAllByUserOrderByBeginDateDesc(User user);

    List<Order> findAllByBeginDateAndStatus(Date beginDate, OrderStatus orderStatus);

    List<Order> findAllByEndDateAndStatus(Date beginDate, OrderStatus orderStatus);

    @Query("SELECT o FROM Order o WHERE o.user.id = ?1")
    List<Order> findAllByUserId(Integer id);
}
