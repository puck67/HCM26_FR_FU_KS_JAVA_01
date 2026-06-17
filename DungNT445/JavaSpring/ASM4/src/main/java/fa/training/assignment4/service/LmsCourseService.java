package fa.training.assignment4.service;

import fa.training.assignment4.entity.LmsCourse;
import fa.training.assignment4.repository.LmsCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LmsCourseService {

    private final LmsCourseRepository courseRepo;

    @Autowired
    public LmsCourseService(LmsCourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    @Transactional
    public void saveAll(List<LmsCourse> courses) {
        courseRepo.saveAll(courses);
    }

    public List<LmsCourse> findAllCourses() {
        return courseRepo.findAll();
    }
    
    @Transactional
    public void deleteAllCourses() {
        courseRepo.deleteAll();
    }
}
