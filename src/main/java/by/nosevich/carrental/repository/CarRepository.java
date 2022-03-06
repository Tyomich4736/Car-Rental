package by.nosevich.carrental.repository;

import by.nosevich.carrental.model.Car;
import by.nosevich.carrental.model.Car_;
import by.nosevich.carrental.model.Category_;
import by.nosevich.carrental.utils.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class CarRepository extends AbstractJpaRepository<Car> {

    @Override
    public Optional<Car> findById(int id) {
        Car car = entityManager.find(Car.class, id);
        return Optional.ofNullable(car);
    }

    @Override
    public List<Car> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Car> query = criteriaBuilder.createQuery(Car.class);
        Root<Car> root = query.from(Car.class);
        CriteriaQuery<Car> select = query.select(root);
        return entityManager.createQuery(select).getResultList();
    }

    public List<Car> findByCategory(Integer categoryId, Page page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Car> query = criteriaBuilder.createQuery(Car.class);
        Root<Car> root = query.from(Car.class);
        CriteriaQuery<Car> select =
                query.select(root).where(criteriaBuilder.equal(root.get(Car_.category).get(Category_.id), categoryId));
        return entityManager.createQuery(select)
                .setFirstResult(page.getOffset())
                .setMaxResults(page.getSize())
                .getResultList();
    }

    public List<Car> findBySubstring(String substring, Page page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Car> query = criteriaBuilder.createQuery(Car.class);
        Root<Car> root = query.from(Car.class);
        CriteriaQuery<Car> select = query.select(root)
                .where(criteriaBuilder.like(criteriaBuilder.lower(root.get(Car_.name)),
                                            prepareSubstringForSearch(substring)))
                .orderBy(criteriaBuilder.desc(root.get(Car_.addingDate)));
        return entityManager.createQuery(select)
                .setFirstResult(page.getOffset())
                .setMaxResults(page.getSize())
                .getResultList();
    }

    public Optional<Car> getLatestAddedCar() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Optional<Date> lastCarAddingDate = getLastCarAddingDate(criteriaBuilder);
        if (!lastCarAddingDate.isPresent()) {
            return Optional.empty();
        }
        CriteriaQuery<Car> query = criteriaBuilder.createQuery(Car.class);
        Root<Car> root = query.from(Car.class);
        CriteriaQuery<Car> select =
                query.select(root).where(criteriaBuilder.equal(root.get(Car_.addingDate), lastCarAddingDate.get()));
        Car car = entityManager.createQuery(select).getSingleResult();
        return Optional.ofNullable(car);
    }

    public long getSearchResultsCount(String substring) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Car> root = query.from(Car.class);
        CriteriaQuery<Long> select = query.select(criteriaBuilder.count(root))
                .where(criteriaBuilder.like(criteriaBuilder.lower(root.get(Car_.name)),
                                            prepareSubstringForSearch(substring)));
        return entityManager.createQuery(select).getSingleResult();
    }

    public long getCategoryCarsCount(Integer categoryId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Car> root = query.from(Car.class);
        CriteriaQuery<Long> select = query.select(criteriaBuilder.count(root))
                .where(criteriaBuilder.equal(root.get(Car_.category).get(Category_.id), categoryId));
        return entityManager.createQuery(select).getSingleResult();
    }

    private Optional<Date> getLastCarAddingDate(CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Date> query = criteriaBuilder.createQuery(Date.class);
        Root<Car> root = query.from(Car.class);
        CriteriaQuery<Date> select = query.select(criteriaBuilder.greatest(root.get(Car_.addingDate)));
        Date result = entityManager.createQuery(select).getSingleResult();
        return Optional.ofNullable(result);
    }

    private String prepareSubstringForSearch(String substring) {
        return "%" + substring.toLowerCase().trim() + "%";
    }
}
