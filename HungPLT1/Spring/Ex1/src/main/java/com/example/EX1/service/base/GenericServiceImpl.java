package com.example.EX1.service.base;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Abstract Generic Service Implementation.
 * Triển khai sẵn 4 thao tác CRUD cơ bản thông qua JpaRepository.
 * Các Service cụ thể chỉ cần kế thừa lớp này.
 *
 * @param <T>  Loại Entity
 * @param <ID> Loại khóa chính
 */
public abstract class GenericServiceImpl<T, ID> implements GenericService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    protected GenericServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

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
