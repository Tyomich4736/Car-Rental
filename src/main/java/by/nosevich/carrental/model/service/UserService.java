package by.nosevich.carrental.model.service;

import java.util.Optional;

import by.nosevich.carrental.model.entities.User;

public interface UserService extends  DAO<User>{
	Optional<User> getByEmail(String email);
}
