package by.nosevich.carrental.controllers.admincontrollers;

import by.nosevich.carrental.entities.Car;
import by.nosevich.carrental.entities.Category;
import by.nosevich.carrental.service.imagestorage.ImageStoreService;
import by.nosevich.carrental.service.car.CarService;
import by.nosevich.carrental.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {

    @Autowired
    private ImageStoreService imageStoreService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CarService carService;

    @GetMapping("/addCategory")
    public String addCategoryForm() {
        return "forAdmin/addCategory";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
        try {
            if(!hasCategoryWithSameName(name)) {
                Category category = new Category();
                imageStoreService.storeCategoryImage(file, category);
                category.setName(name);
                categoryService.save(category);
            }
        } catch(IOException e) {
            return "redirect:/catalog";
        }
        return "redirect:/catalog";
    }

    private boolean hasCategoryWithSameName(String name) {
        for(Category category : categoryService.getAll())
            if(category.getImageName().equals(name)) return true;
        return false;
    }

    @GetMapping("/category/{category}/delete")
    public String deleteCategory(@PathVariable("category") String categoryName) throws IOException {
        Category category = categoryService.getByName(categoryName);
        for(Car car : carService.getByCategory(category)) {
            imageStoreService.deleteAllImagesForCar(car);
            carService.delete(car);
        }
        imageStoreService.deleteCategoryImage(category);
        categoryService.delete(category);
        return "redirect:/catalog";
    }
}
