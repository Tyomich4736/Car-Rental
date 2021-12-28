package by.nosevich.carrental.repository;

import by.nosevich.carrental.entities.Car;
import by.nosevich.carrental.entities.Order;
import by.nosevich.carrental.entities.User;
import by.nosevich.carrental.entities.orderenums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByCarAndStatus(Car car, Status status);

    List<Order> findAllByUserOrderByBeginDateDesc(User user);

    List<Order> findAllByBeginDateAndStatus(Date beginDate, Status status);

    List<Order> findAllByEndDateAndStatus(Date beginDate, Status status);

    @Query("SELECT o FROM Order o WHERE o.user.id = ?1")
    List<Order> findAllByUserId(Integer id);
}
