package by.nosevich.carrental.model.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import by.nosevich.carrental.model.entities.carproperties.*;
import lombok.Data;

@Data
@Entity
public class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Column
	private String name;
	@Column
	private int year;
	@Column
	private Transmission tranmission;
	@Column
	private Color color;
	@Column
	private double fuelConsumption;
	@Column
	private FuelType fuelType;
	@OneToMany(mappedBy = "car")
	private List<CarImage> images = new ArrayList<CarImage>();
	@Column
	private int numberOfSeats;
	@Column
	private double averageSpeed;
	@Column
	private int priceFrom1To3Days;
	@Column
	private int priceFrom4To7Days;
	@Column
	private int priceFrom8To15Days;
	@Column
	private int priceFrom16To30Days;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Category category;
	@OneToMany(mappedBy = "car")
	List<Order> orders = new ArrayList<Order>();
	@OneToOne(mappedBy = "car")
	private Insurance insurance;
}
