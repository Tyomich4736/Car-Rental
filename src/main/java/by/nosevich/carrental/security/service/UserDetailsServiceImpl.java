package by.nosevich.carrental.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import by.nosevich.carrental.model.entities.User;
import by.nosevich.carrental.model.service.UserService;
import by.nosevich.carrental.security.SecurityUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService service;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = service.getByEmail(email).
				orElseThrow(()-> new UsernameNotFoundException("User doesn't exists"));
		return SecurityUser.convertUserToUserDetails(user); 
	}

}