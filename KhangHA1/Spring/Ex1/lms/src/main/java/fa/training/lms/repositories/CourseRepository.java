package fa.training.lms.repositories;

import fa.training.lms.entities.Course;
import fa.training.lms.entities.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, CourseId> {

}
