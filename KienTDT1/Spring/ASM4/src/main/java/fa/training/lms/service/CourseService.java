package fa.training.lms.service;

import fa.training.lms.model.Course;
import fa.training.lms.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repo;

    public CourseService(CourseRepository repo) {
        this.repo = repo;
    }

    public List<Course> getCourses() {
        return repo.findAll();
    }

    public List<Course> saveCourses(List<Course> courses) {
        return repo.saveAll(courses);
    }
}