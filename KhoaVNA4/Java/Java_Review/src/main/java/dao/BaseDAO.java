package dao;

import java.util.List;

public interface BaseDAO<T> {

    boolean add(T entity);

    boolean update(T entity);

    boolean delete(Object id);

    T findById(Object id);

    List<T> findAll();
}
