package by.nosevich.carrental.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.service.UserService;
import by.nosevich.carrental.security.service.UserAuthService;

@Service
public class UserAuthServiceImpl implements UserAuthService{
	
	@Autowired
	private UserService service;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void saveProtectedUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		service.save(user);
	}
	
}
