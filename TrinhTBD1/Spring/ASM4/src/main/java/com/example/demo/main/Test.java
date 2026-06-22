package com.example.demo.main;

import com.example.demo.config.AppConfig;
import com.example.demo.model.Course;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Test {

    @Autowired
    CourseService courseService;

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            Test testApp = context.getBean(Test.class);
            List<Course> courses = new ArrayList<>();
            
            courses.add(new Course("Spring Framework Core", "John Doe", "Learn Spring Core, MVC, and Boot.", 40));
            courses.add(new Course("Java Programming", "Alice Smith", "Master Java OOP, Collections, Streams, and File IO.", 36));
            courses.add(new Course("Microservices Architecture", "Bob J. Johnson", "Build scalable microservices with Spring Cloud.", 52));

            courses.add(null);
            courses.add(new Course("", "Dr. Strange", "Invalid Title", 10));
            courses.add(new Course("Spring Core", "Invalid_Instructor_Name_123", "Invalid Instructor Name Regex", 25));
            courses.add(new Course("JPA and Hibernate", "Jane Doe", "Negative hours duration", -5));
            courses.add(new Course("Design Patterns", "Uncle Bob", "", 15));

            if (testApp.courseService != null) {
                testApp.courseService.saveCourses(courses);
            }

            List<Course> retrievedCourses = testApp.courseService != null ? testApp.courseService.getCourses() : new ArrayList<>();

            if (retrievedCourses != null) {
                retrievedCourses.forEach(Course::displayInfo);
            }
        }
    }
}
