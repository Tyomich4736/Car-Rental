package by.nosevich.carrental.controllers.admincontrollers;

import by.nosevich.carrental.dto.CarDto;
import by.nosevich.carrental.dto.CategoryDto;
import by.nosevich.carrental.service.car.CarService;
import by.nosevich.carrental.service.category.CategoryService;
import by.nosevich.carrental.service.imagestorage.ImageStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminCarController {

    public static final String REDIRECT_ON_CATALOG_PAGE = "redirect:/catalog";
    public static final String ADD_CAR_FORM_PAGE = "admin/addCar";
    public static final String EDIT_CAR_FORM_PAGE = "admin/editCar";
    public static final String REDIRECT_ON_CATALOG_CATEGORY_PAGE = "redirect:/catalog/%d";
    public static final String REDIRECT_ON_CATALOG_CAR_PAGE = "redirect:/catalog/car/%d";
    public static final String ADD_IMAGE_FORM_PAGE = "admin/addImage";
    public static final String REDIRECT_ON_ADD_IMAGE_FORM_PAGE = "redirect:/admin/%d/addImage";
    public static final String REFERER_HEADER = "Referer";
    public static final String REDIRECT_FORMAT = "redirect:%s";
    public static final String CAR_ATTRIBUTE = "car";
    public static final String CURRENT_CATEGORY_ATTRIBUTE = "currentCategory";

    private final ImageStoreService imageStoreService;
    private final CategoryService categoryService;
    private final CarService carService;

    @Autowired
    public AdminCarController(ImageStoreService imageStoreService,
                              CategoryService categoryService,
                              CarService carService) {
        this.imageStoreService = imageStoreService;
        this.categoryService = categoryService;
        this.carService = carService;
    }

    @GetMapping("/{category}/addCar")
    public String addCarForm(@PathVariable("category") Integer categoryId, Model model) {
        Optional<CategoryDto> optionalCategory = categoryService.getById(categoryId);
        if (!optionalCategory.isPresent()) {
            return REDIRECT_ON_CATALOG_PAGE;
        }
        model.addAttribute(CURRENT_CATEGORY_ATTRIBUTE, optionalCategory.get());
        return ADD_CAR_FORM_PAGE;
    }

    @PostMapping("/{currentCategory}/addCar")
    public String addCar(@PathVariable("currentCategory") Integer currentCategoryId,
                         CarDto car,
                         @RequestParam("file") MultipartFile preview) {
        carService.createNewCar(currentCategoryId, car, preview);
        return String.format(REDIRECT_ON_CATALOG_CATEGORY_PAGE, currentCategoryId);
    }

    @GetMapping("/{carId}/edit")
    public String editCarForm(@PathVariable("carId") Integer id, Model model) {
        Optional<CarDto> car = carService.getById(id);
        if (car.isPresent()) {
            model.addAttribute(CAR_ATTRIBUTE, car.get());
            return EDIT_CAR_FORM_PAGE;
        } else {
            return REDIRECT_ON_CATALOG_PAGE;
        }
    }

    @PostMapping("/{carId}/edit")
    public String editCar(@PathVariable("carId") Integer carId, CarDto editedCar) {
        carService.editCar(carId, editedCar);
        return String.format(REDIRECT_ON_CATALOG_CAR_PAGE, carId);
    }

    @GetMapping("/deleteImage/cars/{carId}/{fileName}")
    public String deleteCarImage(@PathVariable("carId") Integer carId, @PathVariable("fileName") String fileName) {
        imageStoreService.deleteCarImageFile(carId, fileName);
        return String.format(REDIRECT_ON_CATALOG_CAR_PAGE, carId);
    }

    @GetMapping("/{carId}/addImage")
    public String addCarImageForm(@PathVariable("carId") Integer carId, Model model) {
        Optional<CarDto> car = carService.getById(carId);
        if (car.isPresent()) {
            model.addAttribute(CAR_ATTRIBUTE, car.get());
            return ADD_IMAGE_FORM_PAGE;
        } else {
            return REDIRECT_ON_CATALOG_PAGE;
        }
    }

    @PostMapping("/{carId}/addImage")
    public String addCarImage(@PathVariable("carId") Integer carId, @RequestParam("file") MultipartFile image) {
        boolean isSuccessful = carService.addCarImage(carId, image);
        if (!isSuccessful) {
            return String.format(REDIRECT_ON_ADD_IMAGE_FORM_PAGE, carId);
        }
        return String.format(REDIRECT_ON_CATALOG_CAR_PAGE, carId);
    }

    @GetMapping("/{carId}/delete")
    public String deleteCar(@PathVariable("carId") Integer carId, HttpServletRequest request) {
        carService.deleteCarById(carId);
        return String.format(REDIRECT_FORMAT, request.getHeader(REFERER_HEADER));
    }
}
