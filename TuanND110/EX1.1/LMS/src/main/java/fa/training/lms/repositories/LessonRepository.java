package fa.training.lms.repositories;

import fa.training.lms.entities.Lesson;
import fa.training.lms.repositories.base.GenericRepository;
import org.springframework.stereotype.Repository;

import fa.training.lms.entities.CourseId;

@Repository
public class LessonRepository extends GenericRepository<Lesson, Long> {
    public LessonRepository() {
        super(Lesson.class);
    }

    public java.util.List<Lesson> findByCourseId(CourseId courseId) {
        return entityManager.createQuery("SELECT l FROM Lesson l WHERE l.course.id = :courseId", Lesson.class)
                .setParameter("courseId", courseId)
                .getResultList();
    }
}
