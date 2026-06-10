package fa.training.ex1_lms.repositories;

import fa.training.ex1_lms.entities.Course;
import fa.training.ex1_lms.entities.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, CourseId> {
}
