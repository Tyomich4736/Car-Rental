package by.nosevich.carrental.model.service.entityservice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import by.nosevich.carrental.model.entities.User;

public interface UserService extends  DAO<User>{
	Optional<User> getByEmail(String email);
	User getByActivationCode(String code);
	void saveProtectedUser(User user);
	List<User> searchLike(String title, Pageable pageable);
	List<User> getAll(Pageable pageable);
}
