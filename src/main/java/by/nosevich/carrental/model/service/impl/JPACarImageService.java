package by.nosevich.carrental.model.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.nosevich.carrental.model.entities.CarImage;
import by.nosevich.carrental.model.repo.CarImageRepository;
import by.nosevich.carrental.model.service.CarImageService;
import by.nosevich.carrental.model.service.CarImageStoreService;

@Service
@Transactional
public class JPACarImageService implements CarImageService{

	@Autowired
	private CarImageStoreService imageStoreService;
	
	@Autowired
	private CarImageRepository repo;
	
	@Override
	public List<CarImage> getAll() {
		return repo.findAll();
	}

	@Override
	public CarImage getById(int id) {
		return repo.getOne(id);
	}

	@Override
	public void delete(CarImage entity) {
		try {
			imageStoreService.deleteImageFile(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		repo.delete(entity);
	}

	@Override
	public void save(CarImage entity) {
		repo.save(entity);
	}
	
}
