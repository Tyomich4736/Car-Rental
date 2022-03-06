package by.nosevich.carrental.service.category;

import by.nosevich.carrental.dto.CategoryDto;
import by.nosevich.carrental.model.Category;
import by.nosevich.carrental.repository.CategoryRepository;
import by.nosevich.carrental.service.imagestorage.ImageStoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class JpaCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ImageStoreService imageStoreService;

    @Autowired
    public JpaCategoryService(CategoryRepository categoryRepository, ImageStoreService imageStoreService) {
        this.categoryRepository = categoryRepository;
        this.imageStoreService = imageStoreService;
    }

    @Override
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream().map(CategoryDto::new).collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDto> getById(int id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.map(CategoryDto::new);
    }

    @Override
    public void delete(CategoryDto categoryDto) {
        categoryRepository.delete(convertDtoToEntity(categoryDto));
    }

    @Override
    public void save(CategoryDto categoryDto) {
        categoryRepository.save(convertDtoToEntity(categoryDto));
    }

    @Override
    public void createAndSaveCategory(String name, MultipartFile previewFile) {
        Category category = new Category();
        category.setName(name);
        try {
            String imagePath = imageStoreService.storeCategoryImageAndReturnPath(previewFile);
            category.setImagePath(imagePath);
            categoryRepository.save(category);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCategoryById(Integer categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            imageStoreService.deleteCategoryImage(category.getImagePath());
            categoryRepository.delete(category);
        }
    }

    private Category convertDtoToEntity(CategoryDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getId() != null) {
            Optional<Category> optionalEntity = categoryRepository.findById(dto.getId());
            if (optionalEntity.isPresent()) {
                Category entity = optionalEntity.get();
                BeanUtils.copyProperties(dto, entity);
                return entity;
            }
        }
        Category entity = new Category();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
