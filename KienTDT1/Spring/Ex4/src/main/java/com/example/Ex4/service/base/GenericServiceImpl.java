package com.example.Ex4.service.base;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public class GenericServiceImpl<T> implements GenericService<T> {

    protected final JpaRepository<T, Long> repo;

    public GenericServiceImpl(JpaRepository<T, Long> repo) {
        this.repo = repo;
    }

    @Override
    public List<T> findAll() {
        return repo.findAll();
    }

    @Override
    public T findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public T save(T entity) {
        return repo.save(entity);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}