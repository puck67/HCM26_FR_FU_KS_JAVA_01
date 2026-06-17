package fa.training.jsfwla102.repository;

import fa.training.jsfwla102.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByStatusOrderByIdDesc(Integer status, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.status = 2 AND LOWER(c.category) LIKE LOWER(concat('%', :category, '%'))")
    Page<Course> findPublishedCoursesByCategory(@Param("category") String category, Pageable pageable);

    long countByStatus(Integer status);
}