package com.example.demo.service.base;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService<T, ID> {

    T save(T entity);

    T findById(ID id);

    void deleteById(ID id);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    long count();
}
