package fa.training.Ex2.service.impl;

import fa.training.Ex2.service.GenericService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class GenericServiceImpl<T, ID, R extends JpaRepository<T, ID>>
        implements GenericService<T, ID> {

    protected final R repository;

    protected GenericServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findById(ID id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }
}