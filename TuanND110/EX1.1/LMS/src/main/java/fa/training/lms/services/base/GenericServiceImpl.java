package fa.training.lms.services.base;

import fa.training.lms.interfaces.base.GenericService;
import fa.training.lms.repositories.base.GenericRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import org.springframework.util.ReflectionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
public abstract class GenericServiceImpl<T, ID> implements GenericService<T, ID> {

    protected final GenericRepository<T, ID> repository;
    protected final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    protected GenericServiceImpl(GenericRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findById(ID id) {
        T entity = repository.findById(id);
        if (entity == null) {
            throw new RuntimeException("Resource not found for ID: " + id);
        }
        return entity;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        if (repository.findById(id) == null) {
            throw new RuntimeException("Cannot update. Resource not found for ID: " + id);
        }
        return repository.update(entity);
    }

    @Override
    public T patch(ID id, Map<String, Object> updates) {
        T existing = findById(id);
        if (existing == null) {
            throw new RuntimeException("Cannot patch. Resource not found for ID: " + id);
        }

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(existing.getClass(), key);
            if (field != null) {
                ReflectionUtils.makeAccessible(field);
                try {
                    Object convertedValue = value == null ? null : objectMapper.convertValue(value, field.getType());
                    ReflectionUtils.setField(field, existing, convertedValue);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to patch field: " + key, e);
                }
            }
        });

        return repository.update(existing);
    }

    @Override
    public void deleteById(ID id) {
        if (repository.findById(id) == null) {
            throw new RuntimeException("Cannot delete. Resource not found for ID: " + id);
        }
        repository.deleteById(id);
    }
}
