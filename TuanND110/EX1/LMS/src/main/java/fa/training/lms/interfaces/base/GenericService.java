package fa.training.lms.interfaces.base;

import java.util.List;
import java.util.Map;

public interface GenericService<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T save(T entity);
    T update(ID id, T entity);
    T patch(ID id, Map<String, Object> updates);
    void deleteById(ID id);
}
