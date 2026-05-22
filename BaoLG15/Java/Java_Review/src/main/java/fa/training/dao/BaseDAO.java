package fa.training.dao;

import java.util.List;

public interface BaseDAO<T, ID> {
    boolean add(T entity);
    List<T> getAll();
    T findById(ID id);
    boolean update(T entity);
    boolean delete(ID id);
}
