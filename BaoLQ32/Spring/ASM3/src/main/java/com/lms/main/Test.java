package com.lms.main;

import com.lms.model.Course;
import com.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Test {

    @Autowired
    private CourseService courseService;

    public static void main(String[] args) {
        // Initialize Spring container
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        // Retrieve the Test bean to run main logic (triggers autowiring)
        Test testApp = context.getBean(Test.class);
        testApp.run();
    }

    public void run() {
        // Create a list of Course objects
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40));
        courses.add(new Course("Java Core", "Jane Smith", "Master Java basics, OOP, Collections, and IO.", 60));
        courses.add(new Course("Spring Advanced", "Bob Johnson", "Deep dive into Spring Security, Cloud, and Microservices.", 50));

        System.out.println("Saving courses to file...");
        courseService.saveCourses(courses);

        System.out.println("Reading courses from file...");
        List<Course> retrievedCourses = courseService.getCourses();

        System.out.println("Displaying retrieved course info:");
        for (Course course : retrievedCourses) {
            course.displayInfo();
        }
    }
}
