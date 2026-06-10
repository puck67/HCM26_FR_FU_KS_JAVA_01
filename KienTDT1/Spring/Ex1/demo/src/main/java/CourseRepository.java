import fa.training.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository {
    Optional<Course> createCourseName(String course_name);
    Optional<Course> saveCourseName(String course_name);
    Optional<Course> getCourseName(String course_name);
    Optional<Course> updateCourseName();
    Optional<Course> deleteCourseName();


    Optional<Object> findBy(String courseName);

    ClassValue<Object> saveAll();
}
