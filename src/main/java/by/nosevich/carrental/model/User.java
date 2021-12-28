package by.nosevich.carrental.model;

import by.nosevich.carrental.model.enums.UserRole;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    private String phoneNumber;

    private boolean active;

    private String activationCode;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Column(unique = true)
    @NotNull
    private String email;
    @NotNull
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<Order>();
}
