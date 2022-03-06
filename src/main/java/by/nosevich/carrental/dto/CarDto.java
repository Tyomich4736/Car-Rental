package by.nosevich.carrental.dto;

import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.enums.FuelType;
import by.nosevich.carrental.model.enums.Transmission;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CarDto {
    private Integer id;
    private String name;
    private Integer year;
    private Transmission transmission;
    private Double fuelConsumption;
    private String previewImagePath;
    private FuelType fuelType;
    private Integer numberOfSeats;
    private Double maxSpeed;
    private Double priceFrom1To3Days;
    private Double priceFrom4To7Days;
    private Double priceFrom8To15Days;
    private Double priceFrom16To30Days;

    public CarDto(){
    }

    public CarDto(Car entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
