package com.springcore;

import com.springcore.config.AppConfig;
import com.springcore.model.Course;
import com.springcore.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Test {

    private final CourseService courseService;

    @Autowired
    public Test(CourseService courseService) {
        this.courseService = courseService;
    }

    public void run() {
        // 1. Create a list of Course objects
        List<Course> courses = Arrays.asList(
                new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40),
                new Course("Java Core OOP", "Jane Smith", "Master OOP concepts, collections, and multi-threading.", 60),
                new Course("Spring Data JPA", "Bob Johnson", "Understand ORM, entities, and database relations.", 30)
        );

        // 2. Call saveCourses() to save them to the file
        System.out.println("--- Saving Courses to file ---");
        courseService.saveCourses(courses);

        // 3. Call getCourses() to get the list of saved courses
        System.out.println("\n--- Loading Courses from file ---");
        List<Course> retrievedCourses = courseService.getCourses();

        // 4. Loop through the retrieved list and call the displayInfo() method
        System.out.println("\n--- Displaying Course Details ---");
        for (Course course : retrievedCourses) {
            course.displayInfo();
            System.out.println("toString(): " + course.toString());
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Create context using AppConfig config class
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        // Retrieve the Test bean and invoke run()
        Test test = context.getBean(Test.class);
        test.run();
    }
}
