package com.example.asm4;

import com.example.asm4.model.Course;
import com.example.asm4.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Test implements CommandLineRunner {

    // Dependency on GenericService for IO operations
    private final GenericService<Course> courseService;

    // Constructor Injection with @Autowired annotation as requested
    @Autowired
    public Test(GenericService<Course> courseService) {
        this.courseService = courseService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Create a list of Course objects
        List<Course> courseList = new ArrayList<>();
        courseList.add(new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40));
        courseList.add(new Course("Java Core", "Alice Smith", "Master Java programming basics.", 60));
        courseList.add(new Course("Design Patterns", "Bob Johnson", "Understand software design patterns.", 30));

        System.out.println("--- Saving Courses to File ---");
        // Save the list using the saveCourses() method from courseService
        courseService.saveCourses(courseList);

        System.out.println("\n--- Loading Courses from File ---");
        // 2. Retrieve the saved list using the getCourses() method
        List<Course> retrievedCourses = courseService.getCourses();

        System.out.println("\n--- Displaying Loaded Courses ---");
        // Loop through the list and call displayInfo() for each course
        for (Course course : retrievedCourses) {
            course.displayInfo();
        }
    }
}
