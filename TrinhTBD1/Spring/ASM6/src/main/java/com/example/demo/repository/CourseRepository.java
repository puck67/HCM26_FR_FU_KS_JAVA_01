package com.example.demo.repository;

import com.example.demo.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByStatusOrderByCreatedDateDesc(Integer status, Pageable pageable);

    Page<Course> findAllByOrderByCreatedDateDesc(Pageable pageable);

    long countByStatus(Integer status);

    @Query("SELECT c FROM Course c WHERE c.status = :status AND " +
           "(LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%')))")
    Page<Course> findByStatusAndCategory(@Param("status") Integer status, 
                                         @Param("category") String category, 
                                         Pageable pageable);
}
