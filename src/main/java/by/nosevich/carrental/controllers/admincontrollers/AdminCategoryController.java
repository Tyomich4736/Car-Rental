package by.nosevich.carrental.controllers.admincontrollers;

import by.nosevich.carrental.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {

    public static final String REDIRECT_ON_CATALOG_PAGE = "redirect:/catalog";
    public static final String ADD_CATEGORY_FORM_PAGE = "admin/addCategory";

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/addCategory")
    public String addCategoryForm() {
        return ADD_CATEGORY_FORM_PAGE;
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam("name") String name,
                              @RequestParam("previewFile") MultipartFile previewFile) {
        categoryService.createAndSaveCategory(name, previewFile);
        return REDIRECT_ON_CATALOG_PAGE;
    }

    @GetMapping("/category/{categoryId}/delete")
    public String deleteCategory(@PathVariable("categoryId") Integer categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return REDIRECT_ON_CATALOG_PAGE;
    }
}
