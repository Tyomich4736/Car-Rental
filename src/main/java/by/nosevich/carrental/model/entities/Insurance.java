package by.nosevich.carrental.model.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "insurance_table")
public class Insurance {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Column
	private String num;
	@Column
	private String insuranceCompany;
	@Temporal(TemporalType.DATE)
	private Date dateOfConclution;
	@Column
	private int sum;
	@Column
	private int paymentPerYear;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Car car;
}
