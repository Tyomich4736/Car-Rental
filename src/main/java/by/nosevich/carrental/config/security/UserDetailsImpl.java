package by.nosevich.carrental.config.security;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import by.nosevich.carrental.model.entities.User;
import lombok.Data;

@Data
public class UserDetailsImpl implements UserDetails{

	private final String username;
	private final String password;
	private final List<SimpleGrantedAuthority> authoryties;
	private final boolean isActive;

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authoryties;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isActive;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isActive;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isActive;
	}

	@Override
	public boolean isEnabled() {
		return isActive;
	}

	public static UserDetails convertUserToUserDetails(User user) {
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				user.isActive(),
				user.isActive(),
				user.isActive(),
				user.isActive(),
				Set.of(new SimpleGrantedAuthority(user.getRole().name())));
	}
	
}
