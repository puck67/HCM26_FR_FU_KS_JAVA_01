package com.lms.repository;

import com.lms.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatusOrderByIdDesc(Integer status, Pageable pageable);
    List<Course> findByStatusOrderByIdDesc(Integer status);

    @Query("SELECT c FROM Course c WHERE c.status = ?1 AND LOWER(c.category) LIKE LOWER(CONCAT('%', ?2, '%')) ORDER BY c.id DESC")
    List<Course> findPublishedByCategory(Integer status, String category);
}
