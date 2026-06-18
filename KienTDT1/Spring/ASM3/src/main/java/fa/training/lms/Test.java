package fa.training.lms;

import fa.training.lms.model.Course;
import fa.training.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Test implements CommandLineRunner {

    @Autowired
    private CourseService courseService;

    @Override
    public void run(String... args) {

        List<Course> courses = new ArrayList<>();

        courses.add(new Course(
                "Spring Framework",
                "John Doe",
                "Learn Spring Core, MVC, and Boot",
                40));

        courses.add(new Course(
                "Java Core",
                "Jane Smith",
                "Master Java Fundamentals",
                30));

        courses.add(new Course(
                "Database Design",
                "David Lee",
                "Learn SQL and Database Modeling",
                25));

        courseService.saveCourses(courses);

        List<Course> savedCourses =
                courseService.getCourses();

        for (Course course : savedCourses) {
            course.displayInfo();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }
}