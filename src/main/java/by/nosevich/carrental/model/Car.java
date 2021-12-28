package by.nosevich.carrental.model;

import by.nosevich.carrental.model.enums.FuelType;
import by.nosevich.carrental.model.enums.Transmission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private String previewImageName;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    private Integer numberOfSeats;

    private Double maxSpeed;

    private Double priceFrom1To3Days;

    private Double priceFrom4To7Days;

    private Double priceFrom8To15Days;

    private Double priceFrom16To30Days;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Category category;
    @OneToMany(mappedBy = "car")
    private List<Order> orders = new ArrayList<Order>();
}
