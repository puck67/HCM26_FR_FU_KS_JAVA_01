package fa.training.dao;

import java.util.List;

public interface GenericDao<T, K> {
    boolean insert(T entity);
    boolean updateById(T entity);
    boolean deleteById(K id);
    T getById(K id);
    List<T> getAll();
}
