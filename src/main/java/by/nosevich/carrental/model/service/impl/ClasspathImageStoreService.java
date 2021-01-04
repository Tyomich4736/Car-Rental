package by.nosevich.carrental.model.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Category;
import by.nosevich.carrental.model.service.ImageStoreService;


@Service
@Transactional
public class ClasspathImageStoreService implements ImageStoreService{
	
	
	@Override
	public void storeCarImage(Car car, MultipartFile fileToUpload) throws IOException {
		String folderPath = getImageStoragePath() + "/cars/"+car.getId()+"/";
		uploadFile(fileToUpload, folderPath);
	}
	
	@Override
	public void storeCarPreview(Car car, MultipartFile fileToUpload) throws IOException {
		String folderPath = getImageStoragePath() + "/cars/"+car.getId()+"/";
		car.setPreviewImageName(fileToUpload.getOriginalFilename());
		uploadFile(fileToUpload, folderPath);
	}	

	@Override
	public void storeCategoryImage(MultipartFile fileToUpload) throws IOException {
		String folderPath = getImageStoragePath() + "/categories/";
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
		String filePath = getImageStoragePath() + "/cars/" + car.getId()
				+ "/" + imageName;
		new File(filePath).delete();
	}

	@Override
	public void deleteAllImagesForCar(Car car) throws IOException {
		String folderPath = getImageStoragePath() + "/cars/"+car.getId();
		File folder = new File(folderPath);
		Arrays.stream(folder.list()).forEach(fileName -> {
			new File(folderPath+"/"+fileName).delete();
		});
		folder.delete();
	}
	
	public void deleteCategoryImage(Category category) throws IOException {
		String imagePath = getImageStoragePath()+"/categories/"+category.getImageName();
		new File(imagePath).delete();
	}

	@Override
	public List<String> getCarImagePaths(Car car) {
		String folderWithImagesPath = getImageStoragePath()+"/cars/"+car.getId();
		File file = new File(folderWithImagesPath);
		return List.of(file.list());
	}

	private String getImageStoragePath() {
		return "src/main/resources/static";
	}
	
}
