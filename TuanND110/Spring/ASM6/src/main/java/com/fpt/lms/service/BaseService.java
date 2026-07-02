package com.fpt.lms.service;

import java.util.List;
import java.util.Optional;

/**
 * Generic base service interface defining common CRUD operations.
 *
 * <p>All service interfaces in this application should extend this
 * to ensure a consistent API contract across entity types.
 *
 * <pre>
 * public interface CourseService extends BaseService&lt;Course, Long&gt; { ... }
 * </pre>
 *
 * @param <T>  the entity type
 * @param <ID> the identifier type (e.g., {@link Long})
 */
public interface BaseService<T, ID> {

    /**
     * Find entity by primary key.
     *
     * @param id the entity identifier
     * @return Optional containing the entity if found
     */
    Optional<T> findById(ID id);

    /**
     * Find entity by primary key or throw if not found.
     *
     * @param id the entity identifier
     * @return the entity
     * @throws jakarta.persistence.EntityNotFoundException if not found
     */
    T findByIdOrThrow(ID id);

    /**
     * Get all entities ordered by most recent first.
     *
     * @return unmodifiable list of all entities
     */
    List<T> findAll();

    /**
     * Persist or merge an entity.
     *
     * @param entity the entity to save
     * @return the saved entity (may have generated ID)
     */
    T save(T entity);

    /**
     * Delete entity by primary key.
     *
     * @param id the entity identifier
     */
    void delete(ID id);
}
