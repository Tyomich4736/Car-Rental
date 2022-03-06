package by.nosevich.carrental.config.security;

import by.nosevich.carrental.dto.UserDto;
import by.nosevich.carrental.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
public class RentalUserDetails implements UserDetails {

    private Integer id;
    private String email;
    private String password;
    private List<SimpleGrantedAuthority> authorities;
    private boolean isActive;

    public RentalUserDetails(UserDto user) {
        id = user.getId();
        email = user.getEmail();
        password = user.getPassword();
        isActive = user.isActive();
        authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

}
