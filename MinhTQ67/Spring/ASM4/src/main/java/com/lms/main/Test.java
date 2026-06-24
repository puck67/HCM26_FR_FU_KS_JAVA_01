package com.lms.main;

import com.lms.model.Course;
import com.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Test class - entry point của ứng dụng
 *
 * Spring IoC container được cấu hình bằng Annotation:
 *   - @Configuration + @ComponentScan: tự động scan và tạo beans
 *   - @Service trên CourseService: đăng ký bean
 *   - @Autowired: Spring inject CourseService vào Test
 */
@Component
public class Test {

    // Spring sẽ inject bean CourseService vào đây (Dependency Injection)
    @Autowired
    private CourseService courseService;

    public void run() {
        System.out.println("========================================");
        System.out.println("   LMS - Learning Management System     ");
        System.out.println("========================================\n");

        // Bước 1: Tạo danh sách courses
        List<Course> courses = Arrays.asList(
            new Course(
                "Spring Framework",
                "John Doe",
                "Learn Spring Core, MVC, and Boot",
                40
            ),
            new Course(
                "Java OOP",
                "Jane Smith",
                "Master Object-Oriented Programming in Java",
                30
            ),
            new Course(
                "Microservices with Spring Boot",
                "Bob Johnson",
                "Build scalable microservices using Spring Boot and Docker",
                50
            )
        );

        // Bước 2: Lưu danh sách courses vào file courses.dat
        System.out.println("--- Saving courses to file ---");
        courseService.saveCourses(courses);

        // Bước 3: Đọc lại từ file và hiển thị thông tin từng course
        System.out.println("\n--- Loading courses from file ---");
        List<Course> loadedCourses = courseService.getCourses();

        System.out.println("\n--- Course List ---");
        for (Course course : loadedCourses) {
            course.displayInfo();
        }

        System.out.println("\n========================================");
        System.out.println("   Done!                                ");
        System.out.println("========================================");
    }

    // -------------------------------------------------------
    // Spring Configuration (Annotation-based, trong cùng file)
    // -------------------------------------------------------
    @Configuration
    @ComponentScan(basePackages = "com.lms")
    static class AppConfig {
        // Spring sẽ tự scan toàn bộ package com.lms
        // và tạo beans cho các class có @Component, @Service, v.v.
    }

    public static void main(String[] args) {
        // Khởi động Spring IoC Container với Annotation config
        ApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);

        // Lấy bean Test từ container và chạy
        Test test = context.getBean(Test.class);
        test.run();

        // Đóng context
        ((AnnotationConfigApplicationContext) context).close();
    }
}
