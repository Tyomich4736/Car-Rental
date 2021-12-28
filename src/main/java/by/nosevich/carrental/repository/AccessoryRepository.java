package by.nosevich.carrental.repository;

import by.nosevich.carrental.model.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessoryRepository extends JpaRepository<Accessory, Integer> {
    Accessory findByName(String name);
}