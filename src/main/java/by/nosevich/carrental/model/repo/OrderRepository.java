package by.nosevich.carrental.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.nosevich.carrental.model.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

}
