package by.nosevich.carrental.model.service.entityservice.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.repo.UserRepository;
import by.nosevich.carrental.model.service.entityservice.UserService;

@Service
@Transactional
public class JPAUserService implements UserService{

	@Autowired
	private UserRepository repo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public List<User> getAll() {
		return repo.findAll();
	}

	@Override
	public User getById(int id) {
		return repo.getOne(id);
	}

	@Override
	public void delete(User entity) {
		repo.delete(entity);
	}

	@Override
	public void save(User entity) {
		repo.save(entity);
	}

	@Override
	public Optional<User> getByEmail(String email) {
		return repo.findByEmail(email);
	}

	@Override
	public User getByActivationCode(String code) {
		return repo.findByActivationCode(code);
	}
	
	@Override
	public void saveProtectedUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		save(user);
	}
	
}
