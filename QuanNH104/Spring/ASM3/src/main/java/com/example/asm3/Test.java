package com.example.asm3;

import com.example.asm3.model.Course;
import com.example.asm3.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Test implements CommandLineRunner {

    // Attribute courseService injected by Spring using Annotation-based configuration (@Autowired)
    @Autowired
    private CourseService courseService;

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // a. Create a list of Course objects (2-3 courses)
        List<Course> courseList = new ArrayList<>();
        courseList.add(new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40));
        courseList.add(new Course("Java Core", "Alice Smith", "Master Java programming basics.", 60));
        courseList.add(new Course("Design Patterns", "Bob Johnson", "Understand software design patterns.", 30));

        System.out.println("--- Saving Courses to File ---");
        // b. Call the saveCourses() method from courseService to save this list to the file
        courseService.saveCourses(courseList);

        System.out.println("\n--- Loading Courses from File ---");
        // c. Call the getCourses() method from courseService to get the list of saved courses
        List<Course> retrievedCourses = courseService.getCourses();

        System.out.println("\n--- Displaying Loaded Courses ---");
        // d. Loop through the retrieved list and call the displayInfo() method for each course
        for (Course course : retrievedCourses) {
            course.displayInfo();
        }
    }
}
