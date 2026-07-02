package com.fpt.lms.repository;

import com.fpt.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for Course entity.
 *
 * Uses @EntityGraph to eagerly fetch instructor/reviews only where needed,
 * preventing N+1 query problem on list views.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    // ── Basic Queries ──────────────────────────────────────────────────────

    Page<Course> findByStatusOrderByCreatedAtDesc(Integer status, Pageable pageable);

    /** All courses ordered newest first — for instructor management list */
    @EntityGraph(attributePaths = {"instructor"})
    List<Course> findAllByOrderByCreatedAtDesc();

    /** Course detail with reviews eagerly loaded — avoid N+1 on detail page */
    @EntityGraph(attributePaths = {"instructor", "reviews"})
    Optional<Course> findWithReviewsById(Long id);

    // ── Category Queries ───────────────────────────────────────────────────

    /**
     * Find published courses by category name (partial match on comma-separated field).
     * Uses CourseStatus.PUBLISHED = 2.
     */
    @Query("SELECT c FROM Course c WHERE c.status = 2 AND c.category LIKE %:cat% ORDER BY c.createdAt DESC")
    List<Course> findPublishedByCategory(@Param("cat") String category);

    /**
     * Returns only the category column strings for published courses.
     * Used by rebuildCategoryStats() to avoid loading full entities.
     */
    @Query("SELECT c.category FROM Course c WHERE c.status = :status AND c.category IS NOT NULL AND c.category != ''")
    List<String> findCategoryStringsByStatus(@Param("status") Integer status);
}
