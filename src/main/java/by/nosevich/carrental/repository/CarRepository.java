package by.nosevich.carrental.repository;

import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findByCategoryOrderByName(Category category, Pageable pageable);

    List<Car> findByCategoryOrderByName(Category category);

    List<Car> findAllByOrderByName();

    List<Car> findByNameContainingIgnoreCase(String title, Pageable pageable);

    List<Car> findAllByOrderByYearDesc(Pageable pageable);
}