package by.nosevich.carrental.service.imagestorage;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class FileSystemImageStoreService implements ImageStoreService {

    public static final String FILE_SEPARATOR = "/";
    public static final String CARS_FOLDER_PATH = "/cars/";
    public static final String CATEGORIES_PATH = "/categories";
    public static final String CATEGORIES_FOLDER_PATH = CATEGORIES_PATH + FILE_SEPARATOR;

    @Value("${image.storage.path}")
    private String imageStoragePath;

    @Override
    public void storeCarImage(Integer carId, MultipartFile fileToUpload) throws IOException {
        String folderPath = imageStoragePath + CARS_FOLDER_PATH + carId;
        uploadFile(fileToUpload, folderPath);
    }

    @Override
    public String storeCategoryImageAndReturnPath(MultipartFile fileToUpload) throws IOException {
        String folderPath = imageStoragePath + CATEGORIES_PATH;
        uploadFile(fileToUpload, folderPath);
        return CATEGORIES_FOLDER_PATH + fileToUpload.getOriginalFilename();
    }

    @Override
    public String storeCarPreviewAndReturnPath(Integer carId, MultipartFile fileToUpload) throws IOException {
        String folderPath = imageStoragePath + CARS_FOLDER_PATH + carId;
        uploadFile(fileToUpload, folderPath);
        return CARS_FOLDER_PATH + carId + FILE_SEPARATOR + fileToUpload.getOriginalFilename();
    }

    @Override
    public List<String> getCarImagePaths(Integer carId) {
        String folderWithImagesPath = imageStoragePath + CARS_FOLDER_PATH + carId;
        File file = new File(folderWithImagesPath);
        if (file.list() != null) {
            return Arrays.stream(file.listFiles())
                    .sorted(Comparator.comparing(File::lastModified))
                    .map(image -> CARS_FOLDER_PATH + carId + FILE_SEPARATOR + image.getName())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteCarImageFile(Integer carId, String fileName) {
        String imageName = String.format("/cars/%d/%s", carId, fileName);
        String filePath = imageStoragePath + imageName;
        new File(filePath).delete();
    }

    @Override
    public void deleteAllImagesForCar(Integer carId) {
        String folderPath = imageStoragePath + CARS_FOLDER_PATH + carId;
        File folder = new File(folderPath);
        folder.delete();
    }

    public void deleteCategoryImage(String categoryImagePath) {
        String imagePath = imageStoragePath + categoryImagePath;
        new File(imagePath).delete();
    }


    private void uploadFile(MultipartFile fileToUpload, String folderPath) throws IOException {
        File file2 = new File(folderPath);
        file2.mkdirs();
        file2 = new File(folderPath + FILE_SEPARATOR + fileToUpload.getOriginalFilename());

        try(InputStream in = fileToUpload.getInputStream();
            OutputStream out = new FileOutputStream(file2)) {
            IOUtils.copy(in, out);
        }
    }
}
