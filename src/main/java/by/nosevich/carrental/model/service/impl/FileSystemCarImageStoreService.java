package by.nosevich.carrental.model.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.CarImage;
import by.nosevich.carrental.model.service.CarImageService;
import by.nosevich.carrental.model.service.CarImageStoreService;


@Service
@Transactional
public class FileSystemCarImageStoreService implements CarImageStoreService{
	
	@Autowired
	private CarImageService carImageService;
	
	@Override
	public void store(Car car, MultipartFile fileToUpload) throws IOException {
		String name = UUID.randomUUID().toString();
		
		String folderPath = getStoragePath() + "\\" + car.getId();
		new File(folderPath).mkdir();		
		
		String filePath = folderPath + "\\" + name + ".jpg";
		File file = new File(filePath);

		try (InputStream in = fileToUpload.getInputStream(); OutputStream out = new FileOutputStream(file)) {
			IOUtils.copy(in, out);
		}
		
		CarImage image = new CarImage();
		image.setImageFileName(name);
		image.setCar(car);
		carImageService.save(image);
	}

	@Override
	public void deleteImageFile(CarImage image) throws IOException {
		String filePath = getStoragePath() 
				+ "\\" + image.getCar().getId()
				+ "\\" + image.getImageFileName()
				+ ".jpg";
		File file = new File(filePath);
		file.delete();
	}

	@Override
	public void deleteImagesForCar(Car car) throws IOException {
		String folderPath = getStoragePath()+"\\"+car.getId();
		File folderWithImages = new File(folderPath);
		folderWithImages.delete();
	}
	
	private String getStoragePath() throws IOException {
		Properties props = new Properties();
		props.load(new FileInputStream("src/main/resources/application.properties"));
		String path = props.getProperty("storage.folder-with-images");
		return path;
	}

}
