package com.example.training.service.base;

import java.util.List;

public interface GenericService<T, ID> {


    List<T> getAll();


    T getById(ID id);


    T save(T entity);


    void delete(ID id);


}