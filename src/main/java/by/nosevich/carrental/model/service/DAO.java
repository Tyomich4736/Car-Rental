package by.nosevich.carrental.model.service;

import java.util.List;

public interface DAO<T> {
	public List<T> getAll();
	public T getById(int id) throws NullPointerException;
	public void delete(T entity);
	public void save(T entity);
}
