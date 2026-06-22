package fa.training.main;

import fa.training.model.Course;
import fa.training.service.CourseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import fa.training.config.AppConfig;

import java.util.Arrays;
import java.util.List;

public class Test {

    private CourseService courseService;

    public Test(CourseService courseService) {
        this.courseService = courseService;
    }

    public static void main(String[] args) {
        // Bootstrap Spring container via annotation-based configuration
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get the CourseService bean from the Spring container
        CourseService courseService = context.getBean(CourseService.class);

        Test test = new Test(courseService);
        test.run();
    }

    private void run() {
        // 1. Create a list of Course objects and save to courses.dat
        List<Course> courses = Arrays.asList(
                new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40),
                new Course("Java OOP", "Jane Smith", "Master Object-Oriented Programming in Java.", 30),
                new Course("Microservices", "Bob Johnson", "Build scalable microservices with Spring Boot.", 50)
        );

        courseService.saveCourses(courses);

        // 2. Retrieve saved list and display each course
        System.out.println("\n--- Courses loaded from file ---");
        List<Course> loadedCourses = courseService.getCourses();
        for (Course course : loadedCourses) {
            course.displayInfo();
        }
    }
}
