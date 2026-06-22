package fa.training.lms.service;

import fa.training.lms.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public void saveCourses(List<Course> courses) {
        if (courses == null || courses.isEmpty()) return;
        courseRepository.saveAll(courses);
    }

    public List<Course> getCourses() {
        List<Course> list = courseRepository.findAll();
        return list == null ? new ArrayList<>() : list;
    }
}