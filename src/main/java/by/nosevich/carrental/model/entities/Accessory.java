package by.nosevich.carrental.model.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accessory_table")
public class Accessory {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	private String name;
	private Double price;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "accessory_order",
    			joinColumns = @JoinColumn(name = "accessory_id"),
    			inverseJoinColumns = @JoinColumn(name = "order_id"))
    private Set<Order> orders = new HashSet<Order>();
}
