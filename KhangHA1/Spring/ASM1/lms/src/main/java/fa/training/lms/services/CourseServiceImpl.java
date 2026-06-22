package fa.training.lms.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import fa.training.lms.model.Course;
import fa.training.lms.repositories.CourseRepository;
import fa.training.lms.services.base.GenericServiceImpl;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, Integer> implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    protected JpaRepository<Course, Integer> getRepository() {
        return courseRepository;
    }
}
