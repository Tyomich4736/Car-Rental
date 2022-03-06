package by.nosevich.carrental.config.security;

import by.nosevich.carrental.dto.UserDto;
import by.nosevich.carrental.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserDto> optionalUser = service.getByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User with given email not found");
        }
        return new RentalUserDetails(optionalUser.get());
    }

}