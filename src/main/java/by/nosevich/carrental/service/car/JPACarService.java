package by.nosevich.carrental.service.car;

import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Category;
import by.nosevich.carrental.model.Order;
import by.nosevich.carrental.repository.CarRepository;
import by.nosevich.carrental.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JPACarService implements CarService {

    @Autowired
    private CarRepository repo;
    @Autowired
    private OrderService orderService;

    @Override
    public List<Car> getAll() {
        return repo.findAllByOrderByName();
    }

    @Override
    public Car getById(int id) throws NullPointerException {
        return repo.getOne(id);
    }

    @Override
    public void delete(Car entity) {
        for(Order order : entity.getOrders()) {
            orderService.delete(order);
        }
        repo.delete(entity);
    }

    @Override
    public void save(Car entity) {
        repo.save(entity);
    }

    @Override
    public List<Car> getByCategory(Category category) {
        return repo.findByCategoryOrderByName(category);
    }

    @Override
    public List<Car> getByCategory(Category category, Pageable pageable) {
        return repo.findByCategoryOrderByName(category, pageable);
    }

    @Override
    public List<Car> searchByNameLike(String title, Pageable pageable) {
        return repo.findByNameContainingIgnoreCase(title, pageable);
    }

    @Override
    public Car getLatest() {
        List<Car> list = repo.findAllByOrderByYearDesc(PageRequest.of(0, 1));
        if(list.size() == 0) return null;
        Car car = list.get(0);
        return car;
    }


}
