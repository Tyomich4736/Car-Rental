package by.nosevich.carrental.service.category;

import by.nosevich.carrental.entities.Category;
import by.nosevich.carrental.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JPACategoryService implements CategoryService {

    @Autowired
    private CategoryRepository repo;

    @Override
    public List<Category> getAll() {
        return repo.findAllByOrderByNameAsc();
    }

    @Override
    public Category getById(int id) {
        return repo.getOne(id);
    }

    @Override
    public void delete(Category entity) {
        repo.delete(entity);
    }

    @Override
    public void save(Category entity) {
        repo.save(entity);
    }

    @Override
    public Category getByName(String name) throws NullPointerException {
        return repo.findByName(name);
    }

}
