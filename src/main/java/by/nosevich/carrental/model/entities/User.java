package by.nosevich.carrental.model.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import by.nosevich.carrental.model.entities.userenums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private Role role;
	@Column(unique = true)
	@NotNull
	private String email;
	@NotNull
	private String password;
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<Order>();
}
