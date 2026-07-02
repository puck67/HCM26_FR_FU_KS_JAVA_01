package com.lms.main;

import com.lms.model.Course;
import com.lms.service.CourseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Entry point demo hệ thống LMS — minh họa IoC/DI với Spring Context.
 * Khởi tạo ApplicationContext, lưu và đọc lại danh sách Course từ file.
 */
@Configuration
@ComponentScan(basePackages = "com.lms")
public class LmsApplication {

    public static void main(String[] args) {
        // Khởi tạo Spring IoC container bằng Annotation-based config
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(LmsApplication.class)) {

            CourseService courseService = context.getBean(CourseService.class);

            // 1. Chuẩn bị dữ liệu mẫu
            List<Course> sampleCourses = java.util.Arrays.asList(
                    new Course("Spring Framework", "John Doe",
                            "Learn Spring Core, MVC, and Boot", 40),
                    new Course("Java OOP", "Jane Smith",
                            "Master Object-Oriented Programming with Java", 30),
                    new Course("Hibernate & JPA", "Bob Nguyen",
                            "Database persistence using Hibernate and JPA", 25)
            );

            printBanner("LEARNING MANAGEMENT SYSTEM (LMS)");

            // 2. Lưu danh sách vào file
            System.out.println("\n--- Saving courses to file ---");
            courseService.saveCourseList(sampleCourses);

            // 3. Đọc lại từ file
            System.out.println("\n--- Reading courses from file ---");
            List<Course> loadedCourses = courseService.loadCourseList();

            // 4. Hiển thị thông tin từng course
            System.out.println("\n--- Course Details ---");
            loadedCourses.forEach(Course::displayCourseInfo);

            System.out.println("\n--- toString() output ---");
            loadedCourses.forEach(System.out::println);

            printBanner("COMPLETED!");
        }
    }

    private static void printBanner(String message) {
        String line = "=".repeat(45);
        System.out.println("\n" + line);
        System.out.printf("  %s%n", message);
        System.out.println(line);
    }
}
