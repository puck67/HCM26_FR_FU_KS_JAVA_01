package fa.training.ex1_lms.repositories;

import fa.training.ex1_lms.entities.Course;
import fa.training.ex1_lms.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourse(Course course);
}
