package by.nosevich.carrental.model.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Column
	private Date begin;
	@Column
	private Date end;
	@Column
	private int price;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private User user;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Car car;
}
