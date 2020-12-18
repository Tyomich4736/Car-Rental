package by.nosevich.carrental.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.nosevich.carrental.model.entities.Insurance;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Integer>{

}
