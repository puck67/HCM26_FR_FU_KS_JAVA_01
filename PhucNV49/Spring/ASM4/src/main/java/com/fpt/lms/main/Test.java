package com.fpt.lms.main;

import com.fpt.lms.model.Course;
import com.fpt.lms.service.CourseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Test {

    @Autowired
    private CourseService courseService;

    public void run() {
        // Create sample courses
        List<Course> courses = Arrays.asList(
            new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40),
            new Course("Java OOP", "Jane Smith", "Master object-oriented programming in Java.", 30),
            new Course("REST API Development", "Bob Johnson", "Build RESTful services with Spring Boot.", 25)
        );

        // Save courses to file
        courseService.saveCourses(courses);

        // Load and display courses
        System.out.println("\n--- Loaded Courses ---");
        List<Course> loaded = courseService.getCourses();
        for (Course c : loaded) {
            c.displayInfo();
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Test test = context.getBean(Test.class);
        test.run();
    }
}
