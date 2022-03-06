package by.nosevich.carrental.service.car;

import by.nosevich.carrental.dto.CarDto;
import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Category;
import by.nosevich.carrental.service.DaoService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarService extends DaoService<CarDto> {

    List<CarDto> getByCategory(Integer categoryId, Integer pageNum);

    List<CarDto> findBySubstring(String searchQuery, Integer pageNum);

    CarDto getLatestAddedCar();

    void createNewCar(Integer categoryId, CarDto car, MultipartFile previewFile);

    void deleteCarById(Integer carId);

    boolean addCarImage(Integer carId, MultipartFile image);

    void editCar(Integer carId, CarDto changedCar);

    boolean hasNextSearchPage(String searchQuery, Integer pageNum);

    boolean hasNextPageForCategory(Integer categoryId, Integer pageNum);
}
