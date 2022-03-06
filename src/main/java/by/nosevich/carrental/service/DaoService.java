package by.nosevich.carrental.service;

import java.util.List;
import java.util.Optional;

public interface DaoService<T> {
    List<T> getAll();

    Optional<T> getById(int id);

    void delete(T dto);

    void save(T dto);
}
