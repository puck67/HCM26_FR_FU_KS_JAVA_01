package fa.training.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {
    Optional<T> getById(ID id);

    List<T> getAll();

    void insert(T entity);

    void updateById(T entity);

    void deleteById(ID id);
}
