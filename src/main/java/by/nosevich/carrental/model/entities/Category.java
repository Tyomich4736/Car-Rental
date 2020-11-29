package by.nosevich.carrental.model.entities;

import java.awt.Image;
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
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Column
	private String name;
	@Column
	private Image preview;
	@OneToMany(mappedBy = "category")
	private List<Car> cars = new ArrayList<Car>();
}
