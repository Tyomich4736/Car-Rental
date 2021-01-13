package by.nosevich.carrental.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import by.nosevich.carrental.model.entities.Accessory;
import by.nosevich.carrental.model.entities.Category;

public interface AccessoryRepository extends JpaRepository<Accessory, Integer>{
	Accessory findByName(String name);
}
