package by.nosevich.carrental.model.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import by.nosevich.carrental.config.properties.ImageStorageProperties;
import by.nosevich.carrental.model.entities.Car;
import by.nosevich.carrental.model.entities.Category;
import by.nosevich.carrental.model.service.ImageStoreService;


@Service
@Transactional
public class FileSystemImageStoreService implements ImageStoreService{
	
	@Autowired
	private ImageStorageProperties props;
	
	@Override
	public void storeCarImage(Car car, MultipartFile fileToUpload) throws IOException {
		String folderPath = props.getStoragePath() + "/cars/"+car.getId();
		uploadFile(fileToUpload, folderPath);
	}
	
	@Override
	public void storeCarPreview(Car car, MultipartFile fileToUpload) throws IOException {
		String folderPath = props.getStoragePath() + "/cars/"+car.getId();
		car.setPreviewImageName("/cars/"+car.getId()+"/"+fileToUpload.getOriginalFilename());
		uploadFile(fileToUpload, folderPath);
	}	

	@Override
	public void storeCategoryImage(MultipartFile fileToUpload, Category category) throws IOException {
		String folderPath = props.getStoragePath() + "/categories";
		category.setImageName("/categories"+"/"+fileToUpload.getOriginalFilename());
		uploadFile(fileToUpload, folderPath);
	}
	
	private void uploadFile(MultipartFile fileToUpload, String folderPath) throws IOException {
		File file2 = new File(folderPath);
		file2.mkdirs();
		file2 = new File(folderPath+"/"+fileToUpload.getOriginalFilename());

		try (InputStream in = fileToUpload.getInputStream(); OutputStream out = new FileOutputStream(file2)) {
			IOUtils.copy(in, out);
		}
	}
	
	@Override
	public void deleteCarImageFile(String imageName) throws IOException {
		String filePath = props.getStoragePath() + imageName;
		new File(filePath).delete();
	}

	@Override
	public void deleteAllImagesForCar(Car car) throws IOException {
		String folderPath = props.getStoragePath() + "/cars/"+car.getId();
		File folder = new File(folderPath);
		Arrays.stream(folder.list()).forEach(fileName -> {
			new File(folderPath+"/"+fileName).delete();
		});
		folder.delete();
	}
	
	public void deleteCategoryImage(Category category) throws IOException {
		String imagePath = props.getStoragePath()+category.getImageName();
		new File(imagePath).delete();
	}

	@Override
	public List<String> getCarImagePaths(Car car) {
		String folderWithImagesPath = props.getStoragePath()+"/cars/"+car.getId();
		File file = new File(folderWithImagesPath);
		List<String> result = new ArrayList<String>();
		for (String fileName : file.list()) {
			result.add("/cars/"+car.getId()+"/"+fileName);
		}
		return result;
	}
	
}
