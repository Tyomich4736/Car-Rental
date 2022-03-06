package by.nosevich.carrental.dto;

import by.nosevich.carrental.model.Order;
import by.nosevich.carrental.model.enums.OrderStatus;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class OrderDto {
    private Integer id;
    private Date beginDate;
    private Date endDate;
    private Double price;
    private OrderStatus orderStatus;
    private UserDto user;
    private CarDto car;
    private Set<AccessoryDto> accessories;

    public OrderDto(){
    }

    public OrderDto(Order entity) {
        BeanUtils.copyProperties(entity, this);
        user = new UserDto(entity.getUser());
        car = new CarDto(entity.getCar());
        accessories = entity.getAccessories().stream().map(AccessoryDto::new).collect(Collectors.toSet());
    }
}
