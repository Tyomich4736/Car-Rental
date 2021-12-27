package by.nosevich.carrental.service.modelservice;

import by.nosevich.carrental.entities.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService extends DAO<User> {
    User getByEmail(String email);

    User getByActivationCode(String code);

    void saveProtectedUser(User user);

    List<User> searchLike(String title, Pageable pageable);

    List<User> getAll(Pageable pageable);
}
