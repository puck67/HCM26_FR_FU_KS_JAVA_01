package fa.training.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fa.training.lms.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}
