package by.nosevich.carrental.dto;

import by.nosevich.carrental.model.Accessory;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
public class AccessoryDto {
    private Integer id;
    private String name;
    private BigDecimal price;

    public AccessoryDto() {
    }

    public AccessoryDto(Accessory entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
