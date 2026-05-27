package com.example.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Generic DAO interface providing standard CRUD operations.
 *
 * @param <T>  Entity type
 * @param <ID> Primary key type (must be Serializable)
 */
public interface GenericDAO<T, ID extends Serializable> {
    void save(T entity);
    void update(T entity);
    void delete(ID id);
    T findById(ID id);
    List<T> findAll();
}
