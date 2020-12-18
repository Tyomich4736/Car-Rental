package by.nosevich.carrental.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.nosevich.carrental.model.entities.Insurance;
import by.nosevich.carrental.model.repo.InsuranceRepository;
import by.nosevich.carrental.model.service.InsuranceService;

@Service
@Transactional
public class JPAInsuranceService implements InsuranceService{

	@Autowired
	private InsuranceRepository repo;
	
	@Override
	public List<Insurance> getAll() {
		return repo.findAll();
	}

	@Override
	public Insurance getById(int id) {
		return repo.getOne(id);
	}

	@Override
	public void delete(Insurance entity) {
		repo.delete(entity);
	}

	@Override
	public void save(Insurance entity) {
		repo.save(entity);
	}

}
