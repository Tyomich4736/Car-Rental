package by.nosevich.carrental.repository;

import by.nosevich.carrental.model.Accessory;
import by.nosevich.carrental.model.Accessory_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class AccessoryRepository extends AbstractJpaRepository<Accessory> {

    @Override
    public Optional<Accessory> findById(int id) {
        Accessory accessory = entityManager.find(Accessory.class, id);
        return Optional.ofNullable(accessory);
    }

    @Override
    public List<Accessory> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Accessory> query = criteriaBuilder.createQuery(Accessory.class);
        Root<Accessory> root = query.from(Accessory.class);
        CriteriaQuery<Accessory> select = query.select(root);
        return entityManager.createQuery(select).getResultList();
    }

    public List<Accessory> findByIds(Collection<Integer> ids) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Accessory> query = criteriaBuilder.createQuery(Accessory.class);
        Root<Accessory> root = query.from(Accessory.class);
        CriteriaQuery<Accessory> select = query.select(root).where(root.get(Accessory_.id).in(ids));
        return entityManager.createQuery(select).getResultList();
    }
}
