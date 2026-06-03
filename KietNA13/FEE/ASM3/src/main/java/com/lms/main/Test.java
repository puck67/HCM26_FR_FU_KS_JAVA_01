package com.lms.main;

import com.lms.config.AppConfig;
import com.lms.model.Course;
import com.lms.service.CourseService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Test {

    private final CourseService courseService;

    public Test(CourseService courseService) {
        this.courseService = courseService;
    }

    public static void main(String[] args) {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            var courseService = ctx.getBean(CourseService.class);
            var app = new Test(courseService);
            app.run();
        }
    }

    private void run() {
        var courses = List.of(
            new Course("Spring Framework",            "John Doe",   "Learn Spring Core, MVC, and Boot",                              40),
            new Course("Java Generics & Collections", "Jane Smith", "Deep dive into Java type system and Collections framework",      30),
            new Course("Microservices with Spring Boot", "Bob Martin", "Build and deploy microservices using Spring Boot & Docker",   60)
        );

        System.out.println("=== Saving courses to courses.dat ===");
        courseService.save(courses);
        System.out.println("Saved " + courses.size() + " courses.\n");

        System.out.println("=== Loading courses from courses.dat ===");
        var loaded = courseService.getAll();

        System.out.println("=== Course List ===");
        loaded.forEach(Course::displayInfo);
    }
}
