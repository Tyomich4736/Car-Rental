package by.nosevich.carrental.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.nosevich.carrental.model.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	Category findByName(String name);
	List<Category> findAllByOrderByNameAsc();
}
