package by.nosevich.carrental.dto;

import by.nosevich.carrental.model.Category;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CategoryDto {
    private Integer id;
    private String name;
    private String imagePath;

    public CategoryDto() {
    }

    public CategoryDto(Category entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
