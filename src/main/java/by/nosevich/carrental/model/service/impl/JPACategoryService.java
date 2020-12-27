package by.nosevich.carrental.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.nosevich.carrental.model.entities.Category;
import by.nosevich.carrental.model.repo.CategoryRepository;
import by.nosevich.carrental.model.service.CategoryService;

@Service
@Transactional
public class JPACategoryService implements CategoryService{

	@Autowired
	private CategoryRepository repo;
	
	@Override
	public List<Category> getAll() {
		return repo.findAll();
	}

	@Override
	public Category getById(int id) {
		return repo.getOne(id);
	}

	@Override
	public void delete(Category entity) {
		repo.delete(entity);
	}

	@Override
	public void save(Category entity) {
		repo.save(entity);
	}
	
}