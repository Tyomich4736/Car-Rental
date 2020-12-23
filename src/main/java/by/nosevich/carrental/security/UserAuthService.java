package by.nosevich.carrental.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.service.UserService;

@Service
public class UserAuthService implements UserDetailsService {

	@Autowired
	private UserService service;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = service.getByEmail(email).
				orElseThrow(()-> new UsernameNotFoundException("User doesn't exists"));
		return SecurityUser.convertUserToUserDetails(user); 
	}
	
	public void saveProtectedUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		service.save(user);
	}

}