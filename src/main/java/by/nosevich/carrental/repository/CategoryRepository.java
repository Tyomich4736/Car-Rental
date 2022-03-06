package by.nosevich.carrental.repository;

import by.nosevich.carrental.model.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository extends AbstractJpaRepository<Category> {
    @Override
    public Optional<Category> findById(int id) {
        Category category = entityManager.find(Category.class, id);
        return Optional.ofNullable(category);
    }

    @Override
    public List<Category> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> query = criteriaBuilder.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);
        CriteriaQuery<Category> select = query.select(root);
        return entityManager.createQuery(select).getResultList();
    }
}
