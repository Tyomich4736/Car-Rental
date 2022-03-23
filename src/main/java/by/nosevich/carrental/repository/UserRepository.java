package by.nosevich.carrental.repository;

import by.nosevich.carrental.model.User;
import by.nosevich.carrental.model.User_;
import by.nosevich.carrental.utils.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends AbstractJpaRepository<User> {

    public static final String SPACE_STRING = " ";

    @Override
    public Optional<User> findById(int id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> from = query.from(User.class);
        CriteriaQuery<User> select = query.select(from);
        return entityManager.createQuery(select).getResultList();
    }

    public Optional<User> findByEmail(String email) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        CriteriaQuery<User> select = query.select(root).where(criteriaBuilder.equal(root.get(User_.email), email));
        List<User> result = entityManager.createQuery(select).getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Optional<User> findByActivationCode(String activationCode) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        CriteriaQuery<User> select =
                query.select(root).where(criteriaBuilder.equal(root.get(User_.activationCode), activationCode));
        List<User> result = entityManager.createQuery(select).getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public List<User> findBySubstring(String searchQuery, Page page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        String searchExpression = "%" + searchQuery.trim().toLowerCase() + "%";
        CriteriaQuery<User> select = query.select(root)
                .where(userLike(criteriaBuilder, root, searchExpression));
        return entityManager.createQuery(select)
                .setFirstResult(page.getOffset())
                .setMaxResults(page.getSize())
                .getResultList();
    }

    public List<User> findAll(Page page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> from = query.from(User.class);
        CriteriaQuery<User> select = query.select(from);
        return entityManager.createQuery(select)
                .setFirstResult(page.getOffset())
                .setMaxResults(page.getSize())
                .getResultList();
    }

    public long getUsersCount() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        CriteriaQuery<Long> select = query.select(criteriaBuilder.count(root));
        return entityManager.createQuery(select).getSingleResult();
    }

    public long getSearchResultsCount(String searchQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        String searchExpression = "%" + searchQuery.trim().toLowerCase() + "%";
        CriteriaQuery<Long> select = query.select(criteriaBuilder.count(root))
                .where(userLike(criteriaBuilder, root, searchExpression));
        return entityManager.createQuery(select).getSingleResult();
    }

    private Predicate userLike(CriteriaBuilder criteriaBuilder, Root<User> root, String searchExpression) {
        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get(User_.firstName)), searchExpression),
                criteriaBuilder.like(criteriaBuilder.lower(root.get(User_.lastName)), searchExpression),
                criteriaBuilder.like(criteriaBuilder.lower(root.get(User_.email)), searchExpression),
                criteriaBuilder.like(criteriaBuilder.concat(
                        criteriaBuilder.concat(criteriaBuilder.lower(root.get(User_.firstName)), SPACE_STRING),
                        criteriaBuilder.lower(root.get(User_.lastName))), searchExpression), criteriaBuilder.like(
                        criteriaBuilder.concat(
                                criteriaBuilder.concat(criteriaBuilder.lower(root.get(User_.lastName)), SPACE_STRING),
                                criteriaBuilder.lower(root.get(User_.firstName))), searchExpression));
    }
}
