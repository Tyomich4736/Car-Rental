package by.nosevich.carrental.service.modelservice;

import by.nosevich.carrental.entities.Car;
import by.nosevich.carrental.entities.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService extends DAO<Car> {
    List<Car> getByCategory(Category category);

    List<Car> getByCategory(Category category, Pageable pageable);

    List<Car> searchByNameLike(String title, Pageable pageable);

    Car getLatest();
}
