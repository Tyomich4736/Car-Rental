package by.nosevich.carrental.service.category;

import by.nosevich.carrental.dto.CategoryDto;
import by.nosevich.carrental.service.DaoService;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService extends DaoService<CategoryDto> {

    void createAndSaveCategory(String name, MultipartFile previewFile);

    void deleteCategoryById(Integer categoryId);
}
