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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import by.nosevich.carrental.model.entities.carproperties.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car_table")
public class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Column
	private String name;
	@Column
	private int year;
	@Column
	@Enumerated(EnumType.STRING)
	private Transmission tranmission;
	@Column
	@Enumerated(EnumType.STRING)
	private Color color;
	@Column
	private double fuelConsumption;
	@Column
	@Enumerated(EnumType.STRING)
	private FuelType fuelType;
	@OneToMany(mappedBy = "car", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
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
	@OneToOne(mappedBy = "car", cascade = CascadeType.REMOVE)
	private Insurance insurance;
}
