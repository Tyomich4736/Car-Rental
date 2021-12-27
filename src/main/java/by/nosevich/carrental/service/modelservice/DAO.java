package by.nosevich.carrental.service.modelservice;

import java.util.List;

public interface DAO<T> {
    public List<T> getAll();

    public T getById(int id) throws NullPointerException;

    public void delete(T entity);

    public void save(T entity);
}
