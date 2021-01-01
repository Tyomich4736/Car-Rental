package by.nosevich.carrental.model.service;

import java.util.List;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Category;

public interface CarService extends DAO<Car>{
	List<Car> getByCategory(Category category);
}
