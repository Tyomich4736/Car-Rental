package by.nosevich.carrental.service.imagestorage;

import by.nosevich.carrental.entities.Car;
import by.nosevich.carrental.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageStoreService {
    void storeCarImage(Car car, MultipartFile file) throws IOException;

    void storeCategoryImage(MultipartFile file, Category category) throws IOException;

    void storeCarPreview(Car car, MultipartFile file) throws IOException;

    List<String> getCarImagePaths(Car car);

    void deleteCarImageFile(String imageName) throws IOException;

    void deleteAllImagesForCar(Car car) throws IOException;

    void deleteCategoryImage(Category category) throws IOException;
}
