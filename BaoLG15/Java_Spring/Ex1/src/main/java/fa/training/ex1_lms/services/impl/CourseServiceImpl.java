package fa.training.ex1_lms.services.impl;

import fa.training.ex1_lms.entities.Course;
import fa.training.ex1_lms.entities.CourseId;
import fa.training.ex1_lms.repositories.CourseRepository;
import fa.training.ex1_lms.services.CourseService;
import fa.training.ex1_lms.services.base.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourseServiceImpl extends GenericServiceImpl<Course, CourseId, CourseRepository> implements CourseService {

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        super(courseRepository);
    }
}
