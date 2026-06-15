package asm1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import asm1.service.GenericService;

import java.util.List;
import java.util.Optional;

public abstract class GenericServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements GenericService<T, ID> {

    @Autowired
    protected R repository;

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
