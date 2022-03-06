package by.nosevich.carrental.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    public static final String REDIRECT_ON_CATALOG_PAGE = "redirect:/catalog";
    public static final String CATALOG_CATEGORIES_PAGE = "catalog/categories";
    public static final String CATALOG_SEARCH_RESULTS_PAGE = "catalog/searchResults";
    public static final String CATALOG_CAR_PAGE = "catalog/carPage";
    public static final String CAR_ATTRIBUTE = "car";
    public static final String IMAGES_ATTRIBUTE = "images";
    public static final String SEARCH_STRING_ATTRIBUTE = "searchString";
    public static final String CURRENT_PAGE_ATTRIBUTE = "currentPage";
    public static final String CARS_ATTRIBUTE = "cars";
    public static final String HAS_NEXT_PAGE_ATTRIBUTE = "hasNextPage";
    public static final String CURRENT_CATEGORY_ATTRIBUTE = "currentCategory";
    public static final String CATEGORIES_ATTRIBUTE = "categories";

    private final CategoryService categoryService;
    private final CarService carService;
    private final ImageStoreService imageStoreService;

    @Autowired
    public CatalogController(CategoryService categoryService,
                             CarService carService,
                             ImageStoreService imageStoreService) {
        this.categoryService = categoryService;
        this.carService = carService;
        this.imageStoreService = imageStoreService;
    }

    @GetMapping("")
    public String getCategories(Model model) {
        model.addAttribute(CATEGORIES_ATTRIBUTE, categoryService.getAll());
        return CATALOG_CATEGORIES_PAGE;
    }

    @GetMapping("/{categoryId}")
    public String getCarsInCategory(Model model,
                                    @PathVariable("categoryId") Integer categoryId,
                                    @RequestParam(value = "page",
                                            required = false,
                                            defaultValue = "1") Integer pageNum) {
        Optional<CategoryDto> optionalCurrentCategory = categoryService.getById(categoryId);
        if (!optionalCurrentCategory.isPresent()) {
            return REDIRECT_ON_CATALOG_PAGE;
        }
        CategoryDto currentCategory = optionalCurrentCategory.get();
        List<CarDto> cars = carService.getByCategory(currentCategory.getId(), pageNum);
        boolean hasNextPage = carService.hasNextPageForCategory(currentCategory.getId(), pageNum);

        model.addAttribute(HAS_NEXT_PAGE_ATTRIBUTE, hasNextPage);
        model.addAttribute(CARS_ATTRIBUTE, cars);
        model.addAttribute(CURRENT_CATEGORY_ATTRIBUTE, currentCategory);
        model.addAttribute(CURRENT_PAGE_ATTRIBUTE, pageNum);
        return "catalog/carList";
    }

    @GetMapping(value = "/search")
    public String searchCars(Model model,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "search") String searchRequest) {
        List<CarDto> cars = carService.findBySubstring(searchRequest, pageNum);
        boolean hasNextSearchPage = carService.hasNextSearchPage(searchRequest, pageNum);
        model.addAttribute(HAS_NEXT_PAGE_ATTRIBUTE, hasNextSearchPage);
        model.addAttribute(CARS_ATTRIBUTE, cars);
        model.addAttribute(CURRENT_PAGE_ATTRIBUTE, pageNum);
        model.addAttribute(SEARCH_STRING_ATTRIBUTE, searchRequest);
        return CATALOG_SEARCH_RESULTS_PAGE;
    }

    @GetMapping("/car/{id}")
    public String getCarPage(Model model, @PathVariable("id") Integer carId) {
        Optional<CarDto> optionalCar = carService.getById(carId);
        if (optionalCar.isPresent()) {
            CarDto car = optionalCar.get();
            List<String> paths = imageStoreService.getCarImagePaths(car.getId());
            model.addAttribute(CAR_ATTRIBUTE, car);
            model.addAttribute(IMAGES_ATTRIBUTE, paths);
            return CATALOG_CAR_PAGE;
        }
        return REDIRECT_ON_CATALOG_PAGE;
    }
}
