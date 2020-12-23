package by.nosevich.carrental.model.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.CarImage;

public interface CarImageStoreService{
	public void store(Car car, MultipartFile file) throws IOException;
	public void deleteImageFile(CarImage image) throws IOException;
	public void deleteImagesForCar(Car car) throws IOException;
}
