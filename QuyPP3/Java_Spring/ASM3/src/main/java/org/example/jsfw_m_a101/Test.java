package org.example.jsfw_m_a101;

import org.example.jsfw_m_a101.model.Course;
import org.example.jsfw_m_a101.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Test implements CommandLineRunner {

    @Autowired
    private CourseService courseService;

    @Override
    public void run(String... args) {
        // Create a list of Course objects
        List<Course> courses = Arrays.asList(
            new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot", 40),
            new Course("Java OOP", "Jane Smith", "Object-Oriented Programming in Java", 30),
            new Course("Microservices", "Bob Johnson", "Building microservices with Spring Cloud", 50)
        );

        // Save to file
        courseService.saveCourses(courses);

        // Load from file
        List<Course> loadedCourses = courseService.getCourses();

        // Display each course
        System.out.println("\n--- Loaded Courses ---");
        for (Course course : loadedCourses) {
            course.displayInfo();
        }
    }
}
