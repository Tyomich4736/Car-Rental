package by.nosevich.carrental.config.security;

import by.nosevich.carrental.entities.userenums.Role;
import by.nosevich.carrental.service.user.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/home", "/register", "/activate/**", "/successfulreg", "/img/**", "/catalog",
                        "/catalog/**", "/rentterms", "/files/**")
                .permitAll()
                .antMatchers("/admin/**")
                .hasAnyAuthority("ADMIN")
                .antMatchers("/users", "/orders")
                .hasAnyAuthority("ADMIN", "EMPLOYEE")
                .antMatchers("/users/set/**")
                .hasAnyAuthority("ADMIN")
                .antMatchers("/order/**")
                .hasAnyAuthority("CLIENT")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(userService.getAll().isEmpty()) {
            userService.saveProtectedUser(
                    new by.nosevich.carrental.entities.User(null, "admin", "admin", null, true, null, Role.ADMIN,
                            "admin@rental.com", "rentalpvt", null));
            userService.saveProtectedUser(
                    new by.nosevich.carrental.entities.User(null, "client", "client", null, true, null, Role.CLIENT,
                            "client@rental.com", "rentalpvt", null));
            userService.saveProtectedUser(
                    new by.nosevich.carrental.entities.User(null, "employee", "employee", null, true, null,
                            Role.EMPLOYEE, "employee@rental.com", "rentalpvt", null));
        }
    }

}
