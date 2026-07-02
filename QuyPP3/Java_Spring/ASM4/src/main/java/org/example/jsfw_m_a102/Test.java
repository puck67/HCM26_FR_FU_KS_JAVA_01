package org.example.jsfw_m_a102;

import org.example.jsfw_m_a102.model.Course;
import org.example.jsfw_m_a102.service.CourseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "org.example.jsfw_m_a102")
public class Test {

    private CourseService courseService;

    public Test(CourseService courseService) {
        this.courseService = courseService;
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Test.class);
        CourseService courseService = context.getBean(CourseService.class);

        // 1. Create and save courses
        List<Course> courses = Arrays.asList(
            new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot", 40),
            new Course("Java OOP", "Jane Smith", "Object-Oriented Programming in Java", 30),
            new Course("Microservices", "Bob Lee", "Building scalable microservices", 50)
        );

        courseService.saveCourses(courses);

        // 2. Load and display courses
        List<Course> loadedCourses = courseService.getCourses();
        loadedCourses.forEach(Course::displayInfo);
    }
}
