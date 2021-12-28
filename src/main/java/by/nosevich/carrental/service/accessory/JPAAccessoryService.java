package by.nosevich.carrental.service.accessory;

import by.nosevich.carrental.model.Accessory;
import by.nosevich.carrental.repository.AccessoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JPAAccessoryService implements AccessoryService {
    @Autowired
    private AccessoryRepository repo;

    @Override
    public List<Accessory> getAll() {
        return repo.findAll();
    }

    @Override
    public Accessory getById(int id) throws NullPointerException {
        return repo.getOne(id);
    }

    @Override
    public void delete(Accessory entity) {
        repo.delete(entity);
    }

    @Override
    public void save(Accessory entity) {
        repo.save(entity);
    }

    @Override
    public Accessory getByName(String name) {
        return repo.findByName(name);
    }
}
