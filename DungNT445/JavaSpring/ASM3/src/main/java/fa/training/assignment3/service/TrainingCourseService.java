package fa.training.assignment3.service;

import fa.training.assignment3.entity.TrainingCourse;
import fa.training.assignment3.repository.TrainingCourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingCourseService {

    private final TrainingCourseRepository courseRepo;

    public TrainingCourseService(TrainingCourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    @Transactional
    public TrainingCourse createCourse(String name, String instructor, String summary, int hours) {
        TrainingCourse course = new TrainingCourse(name, instructor, summary, hours);
        return courseRepo.save(course);
    }

    @Transactional
    public void saveAll(List<TrainingCourse> courses) {
        courseRepo.saveAll(courses);
    }

    public List<TrainingCourse> retrieveAll() {
        return courseRepo.findAll();
    }

    public List<TrainingCourse> searchByInstructor(String instructorName) {
        return courseRepo.findByInstructorFullNameContainingIgnoreCase(instructorName);
    }

    @Transactional
    public void removeAll() {
        courseRepo.deleteAll();
    }
}
