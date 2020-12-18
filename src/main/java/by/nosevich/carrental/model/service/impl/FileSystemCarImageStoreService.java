package by.nosevich.carrental.model.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
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
		//File file2 = new File(name);
		
		Properties props = new Properties();
		props.load(new FileInputStream("src/main/resources/carrental.properties"));
		String path = props.getProperty("carrental.storage.folder-with-images")
				+ "\\" + car.getId();
		new File(path).mkdir();		
		path+="\\" + name + ".jpg";
		File file = new File(path);

		try (InputStream in = fileToUpload.getInputStream(); OutputStream out = new FileOutputStream(file)) {
			IOUtils.copy(in, out);
		}
		
		CarImage image = new CarImage();
		image.setImageFilePath(path);
		image.setCar(car);
		carImageService.save(image);
	}

	@Override
	public List<String> getImagePaths(Car car) throws IOException {
		List<String> paths = new ArrayList<String>();
		car.getImages().forEach(image -> paths.add(image.getImageFilePath()));
		return paths;
	}

}
