package by.nosevich.carrental.model.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Car;

public interface ImageStoreService{
	void storeCarImage(Car car, MultipartFile file) throws IOException;
	void storeCategoryImage(MultipartFile file) throws IOException;
	void storeCarPreview(Car car, MultipartFile file) throws IOException;

	List<String> getCarImagePaths(Car car);
	
	void deleteCarImageFile(Car car, String imageName) throws IOException;
	void deleteAllImagesForCar(Car car) throws IOException;
}
