package com.example.Ex4.service.base;

import java.util.List;

public interface GenericService<T> {
    List<T> findAll();
    T findById(Long id);
    T save(T entity);
    void delete(Long id);
}