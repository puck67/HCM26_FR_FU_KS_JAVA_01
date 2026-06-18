package com.example.ASM3.main;

import com.example.ASM3.model.Course;
import com.example.ASM3.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Configuration
@ComponentScan(basePackages = "com.example.ASM3")
public class Test {

    @Autowired
    private CourseService courseService;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Test.class);

        Test testApp = context.getBean(Test.class);

        testApp.run();

        context.close();
    }

    public void run() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Framework Core", "John Doe", "Learn Spring Core, IoC, DI, and bean configurations.", 40));
        courses.add(new Course("Java Web Development", "Jane Smith", "Learn Servlet, JSP, Spring Boot, and JPA.", 60));
        courses.add(new Course("Database Essentials", "Bob Johnson", "Master SQL databases and H2/MySQL integration.", 30));

        System.out.println("--- Saving courses to file using CourseService ---");

        courseService.saveCourses(courses);

        System.out.println("\n--- Loading courses from file using CourseService ---");

        List<Course> retrievedCourses = courseService.getCourses();

        System.out.println("\n--- Displaying retrieved courses' information ---");

        for (Course course : retrievedCourses) {
            course.displayInfo();
        }
    }
}
