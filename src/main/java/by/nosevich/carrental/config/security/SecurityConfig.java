package by.nosevich.carrental.config.security;

import by.nosevich.carrental.dto.UserDto;
import by.nosevich.carrental.model.enums.UserRole;
import by.nosevich.carrental.service.user.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
                "/catalog/**", "/rentterms", "/files/**", "/changeLang")
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
                .logoutSuccessUrl("/home")
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
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    public void afterPropertiesSet() {
        if (userService.getAll().isEmpty()) {
            UserDto admin = UserDto.builder()
                    .firstName("admin")
                    .lastName("admin")
                    .userRole(UserRole.ADMIN)
                    .email("admin@rental.com")
                    .password("rentalpvt")
                    .active(true)
                    .build();
            UserDto client = UserDto.builder()
                    .firstName("client")
                    .lastName("client")
                    .userRole(UserRole.CLIENT)
                    .email("client@rental.com")
                    .password("rentalpvt")
                    .active(true)
                    .build();
            UserDto employee = UserDto.builder()
                    .firstName("employee")
                    .lastName("employee")
                    .userRole(UserRole.EMPLOYEE)
                    .email("employee@rental.com")
                    .password("rentalpvt")
                    .active(true)
                    .build();
            userService.saveProtectedUser(admin);
            userService.saveProtectedUser(client);
            userService.saveProtectedUser(employee);
        }
    }

}