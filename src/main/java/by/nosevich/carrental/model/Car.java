package by.nosevich.carrental.model;

import by.nosevich.carrental.model.enums.FuelType;
import by.nosevich.carrental.model.enums.Transmission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "car_table")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;

    private Integer year;

    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    private Double fuelConsumption;

    private String previewImagePath;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    private Integer numberOfSeats;

    private Double maxSpeed;

    @Column(nullable = false)
    private Double priceFrom1To3Days;

    @Column(nullable = false)
    private Double priceFrom4To7Days;

    @Column(nullable = false)
    private Double priceFrom8To15Days;

    @Column(nullable = false)
    private Double priceFrom16To30Days;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Category category;

    private Date addingDate;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();
}
