package com.example.EX1.service.base;

import java.util.List;
import java.util.Optional;

/**
 * Generic Service interface dùng chung cho mọi entity.
 * @param <T>  Loại Entity (VD: Course, Lesson)
 * @param <ID> Loại khóa chính (VD: CourseId, Long)
 */
public interface GenericService<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    void deleteById(ID id);
}
