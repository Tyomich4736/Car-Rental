package by.nosevich.carrental.controllers;

import by.nosevich.carrental.entities.Car;
import by.nosevich.carrental.entities.Category;
import by.nosevich.carrental.service.imagestorage.ImageStoreService;
import by.nosevich.carrental.service.car.CarService;
import by.nosevich.carrental.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CarService carService;
    @Autowired
    private ImageStoreService imageStoreService;

    private static final int PAGE_SIZE = 5;

    @GetMapping("")
    public String getCategories(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        return "catalog/categories";
    }

    @GetMapping("/{category}")
    public String getCarsInCategory(Model model, @PathVariable("category") String categoryName, @RequestParam(value = "page", required = false) Integer pageNum) {
        if(pageNum == null) pageNum = 0;
        Category currentCategory = categoryService.getByName(categoryName);
        if(currentCategory == null) return "redirect:/catalog";
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        List<Car> cars = carService.getByCategory(currentCategory, pageable);
        List<Car> nextCars = carService.getByCategory(currentCategory, PageRequest.of(pageNum + 1, PAGE_SIZE));

        if(nextCars.size() != 0) model.addAttribute("hasNextPage", true);
        model.addAttribute("cars", cars);
        model.addAttribute("currentCategory", categoryName);
        model.addAttribute("currentPage", pageNum);
        return "catalog/carList";
    }

    @GetMapping(value = "/search")
    public String getCarsBySeach(Model model, @RequestParam(value = "page", required = false) Integer pageNum, @RequestParam(value = "search") String searchRequest) {
        if(pageNum == null) pageNum = 0;
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        List<Car> cars = carService.searchByNameLike(searchRequest, pageable);
        List<Car> nextCars = carService.searchByNameLike(searchRequest, PageRequest.of(pageNum + 1, PAGE_SIZE));

        if(nextCars.size() != 0) model.addAttribute("hasNextPage", true);
        model.addAttribute("cars", cars);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("search", searchRequest);
        return "catalog/searchResults";
    }

    @GetMapping("/car/{id}")
    public String getCarPage(Model model, @PathVariable("id") String carId) {
        try {
            Car car = carService.getById(Integer.parseInt(carId));
            model.addAttribute("car", car);
            List<String> paths = imageStoreService.getCarImagePaths(car);
            model.addAttribute("images", paths);
        } catch(NullPointerException e) {
            return "redirect:/catalog";
        }
        return "catalog/carPage";
    }
}
