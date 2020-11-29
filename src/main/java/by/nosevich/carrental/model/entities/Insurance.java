package by.nosevich.carrental.model.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Insurance {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Column
	private String num;
	@Column
	private String insuranceCompany;
	@Column
	private Date dateOfConclution;
	@Column
	private int sum;
	@Column
	private int paymentPerYear;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Car car;
}
