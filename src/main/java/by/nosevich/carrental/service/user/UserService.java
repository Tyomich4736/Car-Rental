package by.nosevich.carrental.service.user;

import by.nosevich.carrental.model.User;
import by.nosevich.carrental.service.DaoService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService extends DaoService<User> {
    User getByEmail(String email);

    User getByActivationCode(String code);

    void saveProtectedUser(User user);

    List<User> searchLike(String title, Pageable pageable);

    List<User> getAll(Pageable pageable);
}
