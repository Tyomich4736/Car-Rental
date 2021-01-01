package by.nosevich.carrental.model.service;

import by.nosevich.carrental.model.entities.Category;

public interface CategoryService extends DAO<Category>{
	Category getByName(String name);
}
