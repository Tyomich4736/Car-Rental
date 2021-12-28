package by.nosevich.carrental.repository;

import by.nosevich.carrental.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

    List<Category> findAllByOrderByNameAsc();
}
