package fa.training.lms.services;

import fa.training.lms.entities.Course;
import fa.training.lms.entities.Lesson;
import fa.training.lms.repositories.LessonRepository;
import fa.training.lms.services.base.GenericServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl extends GenericServiceImpl<Lesson, Integer> implements LessonService {
    private final LessonRepository lessonRepository;

    @Override
    protected JpaRepository<Lesson, Integer> getRepository() {
        return lessonRepository;
    }

    @Override
    public List<Lesson> findByCourse(Course course) {
        return lessonRepository.findByCourse(course);
    }
}
