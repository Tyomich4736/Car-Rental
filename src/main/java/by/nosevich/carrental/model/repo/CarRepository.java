package by.nosevich.carrental.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Category;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer>{
	List<Car> findByCategory(Category category);
}
