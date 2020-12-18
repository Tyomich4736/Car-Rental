package by.nosevich.carrental.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.nosevich.carrental.model.entities.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer>{
	
}
