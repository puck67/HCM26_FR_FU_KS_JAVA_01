package fa.training.exercise1.repositories;

import fa.training.exercise1.entities.LmsCourse;
import fa.training.exercise1.entities.LmsLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LmsLessonRepository extends JpaRepository<LmsLesson, Long> {
    List<LmsLesson> findByLmsCourse(LmsCourse course);
}
