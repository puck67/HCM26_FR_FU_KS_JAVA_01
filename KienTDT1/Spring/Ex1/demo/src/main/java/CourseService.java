import fa.training.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Course getCourse(){


        return (Course) courseRepository.findAll();
    }
    public String createCourseName(String course_name){
        return courseRepository.createCourseName(course_name).toString();
    }
    public String saveCourseName(String course_name){
        return courseRepository.saveAll().toString();

    }
    public Optional<Object> updateCourseName(String course_name, int id) throws Throwable {
        Course course= (Course) courseRepository.findById(id).orElseThrow(() ->new RuntimeException("Course not found"));
        course.setCategory(course.getCategory());
        course.setCourse_code(course.getCourse_code());
        course.setCourse_name(course.getCourse_name());
        course.setInstructor(course.getInstructor());
        course.setStart_date(course.getStart_date());
        return (Optional<Object>) courseRepository.save(course_name);
    }
    public Course deleteCourseName(String course_name){
        return courseRepository.deleteCourseName().get();
    }



}
