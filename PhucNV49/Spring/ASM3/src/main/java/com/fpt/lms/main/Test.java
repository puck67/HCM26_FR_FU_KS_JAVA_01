package com.fpt.lms.main;

import com.fpt.lms.model.Course;
import com.fpt.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Test {

    @Autowired
    private CourseService courseService;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfig.class);

        Test test = ctx.getBean(Test.class);
        test.run();

        ctx.close();
    }

    private void run() {
        List<Course> courses = Arrays.asList(
            new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40),
            new Course("Java OOP", "Jane Smith", "Master object-oriented programming in Java.", 30),
            new Course("Hibernate & JPA", "Bob Nguyen", "Database mapping with Hibernate and JPA.", 25)
        );

        courseService.saveCourses(courses);

        System.out.println("\n--- Loading courses from file ---");
        List<Course> loaded = courseService.getCourses();
        for (Course c : loaded) {
            c.displayInfo();
        }
    }
}
