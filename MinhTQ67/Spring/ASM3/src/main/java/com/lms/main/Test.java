package com.lms.main;

import com.lms.model.Course;
import com.lms.service.CourseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.lms")
public class Test {

    // Spring injects CourseService bean via DI
    private CourseService courseService;

    public static void main(String[] args) {
        // Initialize Spring ApplicationContext using Annotation-based configuration
        ApplicationContext context = new AnnotationConfigApplicationContext(Test.class);

        // Get CourseService bean from Spring container (IoC + DI)
        CourseService courseService = context.getBean(CourseService.class);

        // 1. Create list of Course objects
        List<Course> courses = Arrays.asList(
            new Course("Spring Framework", "John Doe",
                "Learn Spring Core, MVC, and Boot", 40),
            new Course("Java OOP", "Jane Smith",
                "Master Object-Oriented Programming with Java", 30),
            new Course("Hibernate & JPA", "Bob Nguyen",
                "Database persistence using Hibernate and JPA", 25)
        );

        System.out.println("===========================================");
        System.out.println("   LEARNING MANAGEMENT SYSTEM (LMS)       ");
        System.out.println("===========================================");

        // 2. Save courses to file
        System.out.println("\n--- Saving courses to file ---");
        courseService.saveCourses(courses);

        // 3. Get courses from file
        System.out.println("\n--- Reading courses from file ---");
        List<Course> loadedCourses = courseService.getCourses();

        // 4. Display info of each course
        System.out.println("\n--- Course Information ---");
        for (Course course : loadedCourses) {
            course.displayInfo();
        }

        System.out.println("\n--- toString() output ---");
        for (Course course : loadedCourses) {
            System.out.println(course);
        }

        System.out.println("\n===========================================");
        System.out.println("             COMPLETED!                    ");
        System.out.println("===========================================");
    }
}
