package com.practice.repository;

import java.io.Serializable;
import java.util.List;

public interface GenericRepository<T, ID extends Serializable> {
    void save(T entity);
    void update(T entity);
    void delete(ID id);
    T findById(ID id);
    List<T> findAll();
}
