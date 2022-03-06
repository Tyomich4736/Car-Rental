package by.nosevich.carrental.service.car;

import by.nosevich.carrental.dto.CarDto;
import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Category;
import by.nosevich.carrental.repository.CarRepository;
import by.nosevich.carrental.repository.CategoryRepository;
import by.nosevich.carrental.service.imagestorage.ImageStoreService;
import by.nosevich.carrental.utils.NullAwareBeanUtils;
import by.nosevich.carrental.utils.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class JpaCarService implements CarService {
    private static final int PAGE_SIZE = 5;

    private final CarRepository carRepository;
    private final CategoryRepository categoryRepository;
    private final ImageStoreService imageStoreService;
    private final NullAwareBeanUtils nullAwareBeanUtils;

    @Autowired
    public JpaCarService(CarRepository carRepository,
                         CategoryRepository categoryRepository,
                         ImageStoreService imageStoreService,
                         NullAwareBeanUtils nullAwareBeanUtils) {
        this.carRepository = carRepository;
        this.categoryRepository = categoryRepository;
        this.imageStoreService = imageStoreService;
        this.nullAwareBeanUtils = nullAwareBeanUtils;
    }

    @Override
    public List<CarDto> getAll() {
        return carRepository.findAll().stream().map(CarDto::new).collect(Collectors.toList());
    }

    @Override
    public Optional<CarDto> getById(int id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.map(CarDto::new);
    }

    @Override
    public void delete(CarDto dto) {
        carRepository.delete(convertDtoToEntity(dto));
    }

    @Override
    public void save(CarDto dto) {
        carRepository.save(convertDtoToEntity(dto));
    }

    @Override
    public List<CarDto> getByCategory(Integer categoryId, Integer pageNum) {
        Page page = new Page(PAGE_SIZE, pageNum);
        List<Car> foundCars = carRepository.findByCategory(categoryId, page);
        return foundCars.stream().map(CarDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CarDto> findBySubstring(String searchQuery, Integer pageNum) {
        if (pageNum == null) {
            pageNum = 1;
        }
        Page page = new Page(PAGE_SIZE, pageNum);
        List<Car> results = carRepository.findBySubstring(searchQuery, page);
        return results.stream().map(CarDto::new).collect(Collectors.toList());
    }

    @Override
    public CarDto getLatestAddedCar() {
        Optional<Car> latestAddedCar = carRepository.getLatestAddedCar();
        return latestAddedCar.map(CarDto::new).orElse(null);
    }

    @Override
    public void createNewCar(Integer categoryId, CarDto car, MultipartFile previewFile) {
        try {
            Car carEntity = convertDtoToEntity(car);
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (optionalCategory.isPresent()) {
                carEntity.setCategory(optionalCategory.get());
                Car savedCar = carRepository.save(carEntity);
                String previewImagePath =
                        imageStoreService.storeCarPreviewAndReturnPath(savedCar.getId(), previewFile);
                savedCar.setPreviewImagePath(previewImagePath);
                carRepository.save(savedCar);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCarById(Integer carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            imageStoreService.deleteAllImagesForCar(car.getId());
            carRepository.delete(car);
        }
    }

    @Override
    public boolean addCarImage(Integer carId, MultipartFile image) {
        Optional<Car> car = carRepository.findById(carId);
        if (car.isPresent()) {
            try {
                imageStoreService.storeCarImage(carId, image);
            } catch(IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public void editCar(Integer carId, CarDto changedCar) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            try {
                nullAwareBeanUtils.copyProperties(changedCar, car);
                carRepository.save(car);
            } catch(IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean hasNextSearchPage(String searchQuery, Integer pageNum) {
        long searchResultsCount = carRepository.getSearchResultsCount(searchQuery);
        return searchResultsCount / (PAGE_SIZE * pageNum) > 0;
    }

    @Override
    public boolean hasNextPageForCategory(Integer categoryId, Integer pageNum) {
        long categoryCarsCount = carRepository.getCategoryCarsCount(categoryId);
        return categoryCarsCount / (PAGE_SIZE * pageNum) > 0;
    }

    private Car convertDtoToEntity(CarDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getId() != null) {
            Optional<Car> optionalEntity = carRepository.findById(dto.getId());
            if (optionalEntity.isPresent()) {
                Car entity = optionalEntity.get();
                BeanUtils.copyProperties(dto, entity);
                return entity;
            }
        }
        Car entity = new Car();
        BeanUtils.copyProperties(dto, entity);
        entity.setAddingDate(new Date());
        return entity;
    }
}
