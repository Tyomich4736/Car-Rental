package by.nosevich.carrental.service;

import java.util.List;

public interface DaoService<T> {
    List<T> getAll();

    T getById(int id);

    void delete(T entity);

    void save(T entity);
}
