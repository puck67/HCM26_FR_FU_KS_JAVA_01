package fa.training.lms.repositories.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public abstract class GenericRepository<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    protected GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public List<T> findAll() {
        return entityManager.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();
    }

    public T findById(ID id) {
        return entityManager.find(entityClass, id);
    }

    public T save(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    public T update(T entity) {
        T merged = entityManager.merge(entity);
        entityManager.flush();
        return merged;
    }

    public void delete(T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        entityManager.flush();
    }

    public void deleteById(ID id) {
        T entity = findById(id);
        if (entity != null) {
            delete(entity);
        }
    }

    public void deleteAll() {
        entityManager.createQuery("delete from " + entityClass.getSimpleName()).executeUpdate();
    }
}
