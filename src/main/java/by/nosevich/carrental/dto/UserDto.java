package by.nosevich.carrental.dto;

import by.nosevich.carrental.model.User;
import by.nosevich.carrental.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean active;
    private String activationCode;
    private UserRole userRole;
    private String email;
    private String password;

    public UserDto() {
    }

    public UserDto(User entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
