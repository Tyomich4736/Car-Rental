package by.nosevich.carrental.model.service.entityservice;

import java.util.List;

import org.springframework.data.domain.Pageable;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Category;

public interface CarService extends DAO<Car>{
	List<Car> getByCategory(Category category);
	List<Car> getByCategory(Category category, Pageable pageable);
	List<Car> searchByNameLike(String title, Pageable pageable);
	Car getLatest();
}
