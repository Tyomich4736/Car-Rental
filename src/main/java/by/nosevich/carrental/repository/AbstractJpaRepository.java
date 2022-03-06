package by.nosevich.carrental.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public abstract class AbstractJpaRepository<E> {

    @PersistenceContext
    protected EntityManager entityManager;

    public E save(E entity) {
        if (entity != null) {
            return entityManager.merge(entity);
        }
        return null;
    }

    public void delete(E entity) {
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    public abstract Optional<E> findById(int id);

    public abstract List<E> findAll();
}
