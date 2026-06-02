package fa.training.dao;

import java.util.List;

/**
 * Generic DAO interface — mirrors the LibraryManagement pattern.
 *
 * @param <T>  entity type
 * @param <ID> primary-key type
 */
public interface BaseDAO<T, ID> {
    boolean add(T entity);
    List<T> getAll();
    T findById(ID id);
    boolean update(T entity);
    boolean delete(ID id);
}
