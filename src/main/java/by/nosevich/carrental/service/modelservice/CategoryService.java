package by.nosevich.carrental.service.modelservice;

import by.nosevich.carrental.entities.Category;

public interface CategoryService extends DAO<Category> {
    Category getByName(String name) throws NullPointerException;
}
