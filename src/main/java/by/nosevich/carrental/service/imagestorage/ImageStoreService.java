package by.nosevich.carrental.service.imagestorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageStoreService {
    void storeCarImage(Integer carId, MultipartFile file) throws IOException;

    String storeCategoryImageAndReturnPath(MultipartFile file) throws IOException;

    String storeCarPreviewAndReturnPath(Integer carId, MultipartFile file) throws IOException;

    List<String> getCarImagePaths(Integer carId);

    void deleteCarImageFile(Integer carId, String fileName);

    void deleteAllImagesForCar(Integer carId);

    void deleteCategoryImage(String categoryImagePath);
}
