package main;

import model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import service.CourseService;

import java.util.Arrays;
import java.util.List;

@Component
public class Test {

    @Autowired
    private CourseService courseService;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Test test = context.getBean(Test.class);

        List<Course> courses = Arrays.asList(
            new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot", 40),
            new Course("Java Basics", "Jane Smith", "Learn Java fundamentals", 30),
            new Course("Spring Boot", "Alice Nguyen", "Build REST APIs with Spring Boot", 25)
        );

        test.courseService.saveCourses(courses);

        List<Course> loaded = test.courseService.getCourses();
        loaded.forEach(Course::displayInfo);
    }
}
