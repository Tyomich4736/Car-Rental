package by.nosevich.carrental.model.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	public void deleteCarImageFile(Car car, String imageName) throws IOException {
		String filePath = "src/main/resources/static/cars/" + car.getId()
				+ "/" + imageName;
		File file = new File(filePath);
		file.delete();
	}

	@Override
	public void deleteAllImagesForCar(Car car) throws IOException {
		String folderPath = "src/main/resources/static/cars/"+car.getId();
		File folderWithImages = new File(folderPath);
		folderWithImages.delete();
	}
	
	@Override
	public void storeCategoryImage(MultipartFile file) throws IOException {
		String folderPath = "src/main/resources/static/categories/";
		uploadFile(file, folderPath);
	}
	
	private void uploadFile(MultipartFile fileToUpload, String folderPath) throws IOException {
		new File(folderPath).mkdir();
		byte[] bytes = fileToUpload.getBytes();
		Path path = Paths.get(folderPath+fileToUpload.getOriginalFilename());
		Files.write(path,bytes);
	}

	

}
