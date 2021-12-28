package by.nosevich.carrental.service.category;

import by.nosevich.carrental.entities.Category;
import by.nosevich.carrental.service.DaoService;

public interface CategoryService extends DaoService<Category> {
    Category getByName(String name) throws NullPointerException;
}
