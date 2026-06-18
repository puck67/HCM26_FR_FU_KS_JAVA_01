package com.example.demo.main;

import com.example.demo.config.AppConfig;
import com.example.demo.model.Course;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Test {

    @Autowired
    CourseService courseService;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Test test = context.getBean(Test.class);

        System.out.println("=== Starting LMS Unit 1 Validation and Integration Tests ===");

        System.out.println("\n[Test 1] Testing invalid Course parameters (Empty Title):");
        try {
            Course invalidCourse = new Course("", "John Doe", "Learn Spring Core", 40);
            List<Course> list = new ArrayList<>();
            list.add(invalidCourse);
            test.courseService.saveCourses(list);
            System.err.println("FAIL: Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n[Test 2] Testing invalid Course parameters (Null/Empty Instructor):");
        try {
            Course invalidCourse = new Course("Spring Framework", "  ", "Learn Spring Core", 40);
            List<Course> list = new ArrayList<>();
            list.add(invalidCourse);
            test.courseService.saveCourses(list);
            System.err.println("FAIL: Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n[Test 3] Testing invalid Course parameters (Negative Duration):");
        try {
            Course invalidCourse = new Course("Spring Framework", "John Doe", "Learn Spring Core", -10);
            List<Course> list = new ArrayList<>();
            list.add(invalidCourse);
            test.courseService.saveCourses(list);
            System.err.println("FAIL: Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n[Test 4] Testing Null List saving:");
        try {
            test.courseService.saveCourses(null);
            System.err.println("FAIL: Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n[Test 5] Testing List with Null Elements saving:");
        try {
            List<Course> invalidList = new ArrayList<>();
            invalidList.add(new Course("Spring Framework", "John Doe", "Learn Spring Core", 40));
            invalidList.add(null);
            test.courseService.saveCourses(invalidList);
            System.err.println("FAIL: Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n[Test 6] Testing wrong data type in file deserialization:");
        try {
            try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream("courses.dat"))) {
                List<String> stringList = new ArrayList<>();
                stringList.add("This is not a Course object");
                oos.writeObject(stringList);
            }
            test.courseService.getCourses();
            System.err.println("FAIL: Expected Exception was not thrown.");
        } catch (Exception e) {
            System.out.println("PASS: Caught expected exception for wrong data type: " + e.getMessage());
        }

        System.out.println("\n[Test 7] Running Required Task 3 Happy Path:");
        
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40));
        courses.add(new Course("Java Programming", "Alice Smith", "Introduction to Java OOP and standard APIs.", 36));
        courses.add(new Course("Web Development", "Bob Johnson", "Learn HTML, CSS, JavaScript, and React.", 50));

        test.courseService.saveCourses(courses);
        System.out.println("Courses saved successfully.");

        List<Course> retrievedCourses = test.courseService.getCourses();
        System.out.println("Retrieved " + retrievedCourses.size() + " courses from the file.");

        System.out.println("\n--- Course Information ---");
        for (Course course : retrievedCourses) {
            course.displayInfo();
        }

        System.out.println("\n=============================================================");
        System.out.println("All validations and happy path completed successfully!");
        System.out.println("=============================================================");
    }
}
