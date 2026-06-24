package com.example.asm6.repository;

import com.example.asm6.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Tìm các khóa học đã publish (status = 2)
    Page<Course> findByStatus(Integer status, Pageable pageable);
    
    // Tìm các khóa học đã publish và chứa một category cụ thể
    @Query("SELECT c FROM Course c WHERE c.status = :status AND LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%'))")
    Page<Course> findByStatusAndCategoryLike(@Param("status") Integer status, @Param("category") String category, Pageable pageable);
}
