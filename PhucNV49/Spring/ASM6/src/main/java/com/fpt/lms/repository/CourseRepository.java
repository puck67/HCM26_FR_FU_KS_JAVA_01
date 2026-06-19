package com.fpt.lms.repository;

import com.fpt.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatusOrderByCreatedAtDesc(Integer status, Pageable pageable);
    List<Course> findAllByOrderByCreatedAtDesc();
    @Query("SELECT c FROM Course c WHERE c.status = 2 AND (c.category LIKE %:cat% ) ORDER BY c.createdAt DESC")
    List<Course> findPublishedByCategory(@Param("cat") String category);
}
