package by.nosevich.carrental.repository;

import by.nosevich.carrental.model.Car_;
import by.nosevich.carrental.model.Order;
import by.nosevich.carrental.model.Order_;
import by.nosevich.carrental.model.User_;
import by.nosevich.carrental.model.enums.OrderStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository extends AbstractJpaRepository<Order> {
    @Override
    public Optional<Order> findById(int id) {
        Order order = entityManager.find(Order.class, id);
        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        CriteriaQuery<Order> select = query.select(root);
        return entityManager.createQuery(select).getResultList();
    }

    public Optional<Order> findByUserAndStatus(String userEmail, OrderStatus status) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        CriteriaQuery<Order> select = query.select(root)
                .where(criteriaBuilder.and(criteriaBuilder.equal(root.get(Order_.user).get(User_.email), userEmail),
                                           criteriaBuilder.equal(root.get(Order_.orderStatus), status)));
        List<Order> result = entityManager.createQuery(select).getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }

    public List<Order> findByCarAndStatuses(Integer carId, OrderStatus... statuses) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        CriteriaQuery<Order> select = query.select(root)
                .where(criteriaBuilder.and(criteriaBuilder.equal(root.get(Order_.car).get(Car_.id), carId),
                                           root.get(Order_.orderStatus).in(statuses)));
        return entityManager.createQuery(select).getResultList();
    }

    public List<Order> findAllByUserEmail(String userEmail) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        CriteriaQuery<Order> select = query.select(root)
                .where(criteriaBuilder.equal(root.get(Order_.user).get(User_.email), userEmail))
                .orderBy(criteriaBuilder.desc(root.get(Order_.beginDate)));
        return entityManager.createQuery(select).getResultList();
    }

    public List<Order> findAllByUserId(Integer userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        CriteriaQuery<Order> select = query.select(root)
                .where(criteriaBuilder.equal(root.get(Order_.user).get(User_.id), userId))
                .orderBy(criteriaBuilder.desc(root.get(Order_.beginDate)));
        return entityManager.createQuery(select).getResultList();
    }

    public List<Order> findByDateAndStatuses(Date date, OrderStatus... statuses) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        CriteriaQuery<Order> select = query.select(root)
                .where(criteriaBuilder.and(criteriaBuilder.or(criteriaBuilder.equal(root.get(Order_.beginDate), date),
                                                              criteriaBuilder.equal(root.get(Order_.endDate), date)),
                                           root.get(Order_.orderStatus).in(statuses)));
        return entityManager.createQuery(select).getResultList();
    }
}
