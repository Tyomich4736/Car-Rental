package by.nosevich.carrental.model.service.entityservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.nosevich.carrental.model.entities.Accessory;
import by.nosevich.carrental.model.repo.AccessoryRepository;
import by.nosevich.carrental.model.service.entityservice.AccessoryService;

@Service
@Transactional
public class JPAAccessoryService implements AccessoryService{
	@Autowired
	private AccessoryRepository repo;

	@Override
	public List<Accessory> getAll() {
		return repo.findAll();
	}

	@Override
	public Accessory getById(int id) throws NullPointerException {
		return repo.getOne(id);
	}

	@Override
	public void delete(Accessory entity) {
		repo.delete(entity);
	}

	@Override
	public void save(Accessory entity) {
		repo.save(entity);
	}

	@Override
	public Accessory getByName(String name) {
		return repo.findByName(name);
	}
}
