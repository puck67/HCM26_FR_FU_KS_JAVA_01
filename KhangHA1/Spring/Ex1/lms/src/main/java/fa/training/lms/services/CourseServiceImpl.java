package fa.training.lms.services;

import fa.training.lms.entities.Course;
import fa.training.lms.entities.CourseId;
import fa.training.lms.repositories.CourseRepository;
import fa.training.lms.services.base.GenericServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends GenericServiceImpl<Course, CourseId> implements CourseService {
    private final CourseRepository courseRepository;

    @Override
    protected JpaRepository<Course, CourseId> getRepository() {
        return courseRepository;
    }
}
