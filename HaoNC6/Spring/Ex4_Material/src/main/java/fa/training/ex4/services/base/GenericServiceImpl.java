package fa.training.ex4.services.base;

import fa.training.ex4.mappers.GenericMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public abstract class GenericServiceImpl<T, ID, REQ_C, REQ_U, RES> implements GenericService<ID, REQ_C, REQ_U, RES> {

    protected final JpaRepository<T, ID> repository;
    protected final GenericMapper<T, REQ_C, REQ_U, RES> mapper;

    @Override
    public List<RES> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public RES findById(ID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
    }

    @Override
    @Transactional
    public RES create(REQ_C request) {
        T entity = mapper.toEntity(request);
        T savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public RES update(ID id, REQ_U request) {
        T existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));

        mapper.updateEntityFromRequest(request, existingEntity);
        T updatedEntity = repository.save(existingEntity);
        return mapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Resource not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
