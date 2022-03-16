package by.nosevich.carrental.service.user;

import by.nosevich.carrental.dto.UserDto;
import by.nosevich.carrental.exceptions.IncorrectUserDataException;
import by.nosevich.carrental.model.User;
import by.nosevich.carrental.model.enums.UserRole;
import by.nosevich.carrental.repository.UserRepository;
import by.nosevich.carrental.service.mailsender.MailService;
import by.nosevich.carrental.utils.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
public class JPAUserService implements UserService {

    private static final int PAGE_SIZE = 20;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Autowired
    public JPAUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDto::new).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserDto::new);
    }

    @Override
    public void delete(UserDto user) {
        userRepository.delete(convertDtoToEntity(user));
    }

    @Override
    public void save(UserDto user) {
        userRepository.save(convertDtoToEntity(user));
    }

    @Override
    public void createNewUser(UserDto user, String passwordConfirmation)
    throws IncorrectUserDataException, MessagingException {
        if (!userDataIsCorrect(user, passwordConfirmation)) {
            throw new IncorrectUserDataException();
        }
        user.setActivationCode(UUID.randomUUID().toString());
        user.setUserRole(UserRole.CLIENT);
        user.setActive(false);
        mailService.sendActivationMessage(user);
        saveProtectedUser(user);
    }

    @Override
    public Optional<UserDto> getByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(UserDto::new);
    }

    @Override
    public void activateUser(String activationCode) {
        Optional<User> optionalUser = userRepository.findByActivationCode(activationCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(true);
            userRepository.save(user);
        }
    }

    @Override
    public void saveProtectedUser(UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
    }

    @Override
    public List<UserDto> findBySubstring(String searchQuery, Integer pageNum) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return getAll(pageNum);
        } else {
            Page page = new Page(PAGE_SIZE, pageNum);
            List<User> result = userRepository.findBySubstring(searchQuery, page);
            return result.stream().map(UserDto::new).collect(Collectors.toList());
        }
    }

    @Override
    public boolean hasNextSearchPage(String searchQuery, Integer pageNum) {
        long searchResultsCount;
        if (searchQuery == null) {
            searchResultsCount = userRepository.getUsersCount();
        } else {
            searchResultsCount = userRepository.getSearchResultsCount(searchQuery);
        }
        return searchResultsCount / (PAGE_SIZE * pageNum) > 0;
    }

    @Override
    public List<UserDto> getAll(Integer pageNum) {
        if (pageNum == null) {
            pageNum = 1;
        }
        Page page = new Page(PAGE_SIZE, pageNum);
        List<User> results = userRepository.findAll(page);
        return results.stream().map(UserDto::new).collect(Collectors.toList());
    }

    @Override
    public void makeAnEmployee(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserRole(UserRole.EMPLOYEE);
            userRepository.save(user);
        }
    }

    @Override
    public void makeAnClient(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserRole(UserRole.CLIENT);
            userRepository.save(user);
        }
    }

    private boolean userDataIsCorrect(UserDto user, String confirmPassword) {
        return !user.getEmail().trim().equals("") && !user.getFirstName().trim().equals("") &&
                !user.getLastName().trim().equals("") && !user.getPhoneNumber().trim().equals("") &&
                user.getPassword().equals(confirmPassword);
    }

    private User convertDtoToEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getId() != null) {
            Optional<User> optionalEntity = userRepository.findById(dto.getId());
            if (optionalEntity.isPresent()) {
                User entity = optionalEntity.get();
                BeanUtils.copyProperties(dto, entity);
                return entity;
            }
        }
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
