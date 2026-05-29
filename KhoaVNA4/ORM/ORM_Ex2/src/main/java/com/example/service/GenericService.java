package com.example.service;

import java.util.List;

public interface GenericService<T, ID> {
    void save(T entity);
    void update(T entity);
    void delete(ID id);
    T getById(ID id);
    List<T> getAll();
}
