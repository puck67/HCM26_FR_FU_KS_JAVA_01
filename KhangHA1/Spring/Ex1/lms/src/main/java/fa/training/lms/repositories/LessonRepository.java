package fa.training.lms.repositories;

import fa.training.lms.entities.Course;
import fa.training.lms.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByCourse(Course course);
}