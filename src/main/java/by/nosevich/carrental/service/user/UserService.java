package by.nosevich.carrental.service.user;

import by.nosevich.carrental.dto.UserDto;
import by.nosevich.carrental.exceptions.IncorrectUserDataException;
import by.nosevich.carrental.exceptions.UserWithSameEmailAlreadyExistsException;
import by.nosevich.carrental.service.DaoService;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface UserService extends DaoService<UserDto> {

    void createNewUser(UserDto user, String passwordConfirmation, Locale locale)
    throws IncorrectUserDataException, MessagingException, UserWithSameEmailAlreadyExistsException;

    Optional<UserDto> getByEmail(String email);

    void activateUser(String activationCode);

    void saveProtectedUser(UserDto user);

    List<UserDto> findBySubstring(String searchQuery, Integer pageNum);

    boolean hasNextSearchPage(String searchQuery, Integer pageNum);

    List<UserDto> getAll(Integer pageNum);

    void makeAnEmployee(Integer userId);

    void makeAClient(Integer userId);
}
