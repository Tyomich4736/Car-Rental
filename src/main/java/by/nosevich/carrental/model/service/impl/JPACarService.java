package by.nosevich.carrental.model.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.repo.CarRepository;
import by.nosevich.carrental.model.service.CarService;
import by.nosevich.carrental.model.service.ImageStoreService;

@Service
@Transactional
public class JPACarService implements CarService {

	private ImageStoreService imageStoreService;
	
	@Autowired
	private CarRepository repo;
	
	@Override
	public List<Car> getAll() {
		return repo.findAll();
	}

	@Override
	public Car getById(int id) {
		return repo.getOne(id);
	}

	@Override
	public void delete(Car entity) {
		try {
			imageStoreService.deleteAllImagesForCar(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		repo.delete(entity);
	}

	@Override
	public void save(Car entity) {
		repo.save(entity);
	}

}
