import fa.training.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController

public class CourseController {

    @Autowired private CourseService courseService;

    @RequestMapping("/createCourse/{id}")
    public String saveCourse(@RequestBody Course course){
        return courseService.saveCourseName(String.valueOf(course));

    }

    @GetMapping("/course")
    public List<Course> courseList(){
        return Collections.singletonList(courseService.getCourse());
    }

    @PutMapping("/course/{id}")
    public Optional<Object> updateCourse (@RequestBody String course, @PathVariable("id") int id ) throws Throwable {
        return courseService.updateCourseName(
                course,id);

    }

    @DeleteMapping("/course/{id}")
    public String deleteCourseById(@PathVariable("id") int id){
        courseService.deleteCourseName(String.valueOf(id));
        return "Deleted successfully";

    }





}
