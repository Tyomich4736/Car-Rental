package by.nosevich.carrental.model.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Car;

public interface CarImageStoreService{
	public void store(Car car, MultipartFile file) throws IOException;
	public List<String> getImagePaths(Car car) throws IOException;
}
