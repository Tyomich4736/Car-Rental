package by.nosevich.carrental.model.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Column
	private String name;
	@Column
	private String phoneNumber;
	@Column
	private String email;
	@Column
	private String password;
	@Column
	private Date birthdate;
	@OneToMany(mappedBy = "user")
	private List<Order> orders = new ArrayList<Order>();
}
