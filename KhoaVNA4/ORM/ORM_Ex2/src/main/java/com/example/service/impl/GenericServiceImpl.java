package com.example.service.impl;

import com.example.dao.GenericDAO;
import com.example.service.GenericService;
import java.util.List;

public abstract class GenericServiceImpl<T, ID, DAO extends GenericDAO<T, ID>> implements GenericService<T, ID> {
    protected final DAO dao;

    protected GenericServiceImpl(DAO dao) {
        this.dao = dao;
    }

    @Override
    public void save(T entity) {
        dao.save(entity);
    }

    @Override
    public void update(T entity) {
        dao.update(entity);
    }

    @Override
    public void delete(ID id) {
        dao.delete(id);
    }

    @Override
    public T getById(ID id) {
        return dao.findById(id);
    }

    @Override
    public List<T> getAll() {
        return dao.findAll();
    }
}
