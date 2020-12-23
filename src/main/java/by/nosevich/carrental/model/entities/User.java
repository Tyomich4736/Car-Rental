package by.nosevich.carrental.model.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import by.nosevich.carrental.model.entities.userproperties.Role;
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
	
	private String firstName;
	
	private String lastName;
	
	private String phoneNumber;
	
	private boolean active;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	@Column(unique = true)
	private String email;

	private String password;
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Order> orders = new ArrayList<Order>();
}
