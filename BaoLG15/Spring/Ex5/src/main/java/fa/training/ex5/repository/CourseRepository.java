package fa.training.ex5.repository;

import fa.training.ex5.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByCourseNameContainingIgnoreCase(String keyword, Pageable pageable);
}
