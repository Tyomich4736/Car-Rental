package by.nosevich.carrental.service.user;

import by.nosevich.carrental.entities.User;
import by.nosevich.carrental.repository.UserRepository;
import by.nosevich.carrental.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JPAUserService implements UserService {

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
    public User getByEmail(String email) {
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

    @Override
    public List<User> searchLike(String title, Pageable pageable) {
        return repo.findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(title,
                title, title, pageable);
    }

    @Override
    public List<User> getAll(Pageable pageable) {
        return repo.findAll(pageable).toList();
    }
}
