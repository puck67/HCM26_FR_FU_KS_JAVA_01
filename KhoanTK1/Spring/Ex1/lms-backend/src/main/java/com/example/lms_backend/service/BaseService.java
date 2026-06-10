package com.example.lms_backend.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    T save(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    T update(ID id, T entity);
    void deleteById(ID id);
}
