package by.nosevich.carrental.model.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.service.ImageStoreService;


@Service
@Transactional
public class ClasspathImageStoreService implements ImageStoreService{
	
	
	@Override
	public void storeCarImage(Car car, MultipartFile fileToUpload) throws IOException {
		String folderPath = "src/main/resources/static/cars/"+car.getId()+"/";
		uploadFile(fileToUpload, folderPath);
	}
	
	@Override
	public void storeCarPreview(Car car, MultipartFile fileToUpload) throws IOException {
		String folderPath = "src/main/resources/static/cars/"+car.getId()+"/";
		car.setPreviewImageName(fileToUpload.getOriginalFilename());
		uploadFile(fileToUpload, folderPath);
	}	

	@Override
	public void storeCategoryImage(MultipartFile fileToUpload) throws IOException {
		String folderPath = "src/main/resources/static/categories/";
		uploadFile(fileToUpload, folderPath);
	}
	
	private void uploadFile(MultipartFile fileToUpload, String folderPath) throws IOException {
		new File(folderPath).mkdir();
		byte[] bytes = fileToUpload.getBytes();
		Path path = Paths.get(folderPath+fileToUpload.getOriginalFilename());
		Files.write(path,bytes);
	}
	
	
	@Override
	public void deleteCarImageFile(Car car, String imageName) throws IOException {
		String filePath = "src/main/resources/static/cars/" + car.getId()
				+ "/" + imageName;
		delete(filePath);
	}

	@Override
	public void deleteAllImagesForCar(Car car) throws IOException {
		String folderPath = "src/main/resources/static/cars/"+car.getId();
		delete(folderPath);
	}
	
	private void delete(String filePath) {
		File file = new File(filePath);
		file.delete();
	}
}
