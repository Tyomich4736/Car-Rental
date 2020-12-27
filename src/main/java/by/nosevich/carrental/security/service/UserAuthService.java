package by.nosevich.carrental.security.service;

import by.nosevich.carrental.model.entities.User;

public interface UserAuthService {
	void saveProtectedUser(User user);
}
