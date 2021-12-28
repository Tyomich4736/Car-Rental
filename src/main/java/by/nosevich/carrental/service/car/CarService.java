package by.nosevich.carrental.service.car;

import by.nosevich.carrental.entities.Car;
import by.nosevich.carrental.entities.Category;
import by.nosevich.carrental.service.DaoService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService extends DaoService<Car> {
    List<Car> getByCategory(Category category);

    List<Car> getByCategory(Category category, Pageable pageable);

    List<Car> searchByNameLike(String title, Pageable pageable);

    Car getLatest();
}
