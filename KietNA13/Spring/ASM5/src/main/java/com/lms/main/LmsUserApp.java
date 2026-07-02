package com.lms.main;

import com.lms.model.Instructor;
import com.lms.model.LmsUser;
import com.lms.model.Student;
import com.lms.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Entry point demo hệ thống LMS — minh họa Spring IoC/DI với @Component + @Autowired.
 *
 * Spring Annotation-based Configuration:
 *   @Component     → Spring tạo bean cho LmsUserApp
 *   @Autowired     → Spring inject LmsDataService vào dataService (DI)
 *   @Service       → Spring tạo bean cho LmsDataService
 *   @ComponentScan → Spring tự scan package com.lms tìm tất cả bean
 */
@Component
public class LmsUserApp {

    // Spring inject bean LmsDataService vào đây (Dependency Injection)
    @Autowired
    private LmsDataService dataService;

    public void run() {
        printBanner("LMS - User Management System");

        // ── Bước 1: Tạo và lưu danh sách LmsUser (kể cả Instructor) ─────────
        List<LmsUser> users = List.of(
                new LmsUser(1L, "Alice Nguyen", "alice@fpt.com"),
                new Instructor(2L, "John Doe",   "john.doe@fpt.com",   "Engineering",  "Spring Framework Expert"),
                new Instructor(3L, "Jane Smith", "jane.smith@fpt.com", "Data Science",  "Machine Learning Researcher"),
                new LmsUser(4L, "Bob Tran", "bob.tran@fpt.com")
        );

        System.out.println("\n--- [1] Saving users to users.dat ---");
        dataService.saveUserList(users);

        // ── Bước 2: Tạo và lưu danh sách Student ─────────────────────────────
        List<Student> students = List.of(
                new Student(101L, "Jane Smith",  3.75),
                new Student(102L, "Minh Le",     3.50),
                new Student(103L, "Huong Pham",  3.90),
                new Student(104L, "David Tran",  2.80)
        );

        System.out.println("\n--- [2] Saving students to students.dat ---");
        dataService.saveStudentList(students);

        // ── Bước 3: Đọc lại users và gọi displayUserInfo() — Polymorphism demo ─
        System.out.println("\n--- [3] Loading & displaying users (Polymorphism demo) ---");
        List<LmsUser> loadedUsers = dataService.loadUserList();
        loadedUsers.forEach(LmsUser::displayUserInfo); // Java gọi đúng method theo kiểu thực tế

        // ── Bước 4: Đọc lại students và gọi displayStudentInfo() ─────────────
        System.out.println("\n--- [4] Loading & displaying students ---");
        List<Student> loadedStudents = dataService.loadStudentList();
        loadedStudents.forEach(Student::displayStudentInfo);

        printBanner("Done!");
    }

    private static void printBanner(String message) {
        String line = "=".repeat(55);
        System.out.println("\n" + line);
        System.out.printf("   %s%n", message);
        System.out.println(line);
    }

    // ── Spring Annotation-based Configuration (inner class) ───────────────────
    @Configuration
    @ComponentScan(basePackages = "com.lms")
    static class AppConfig {
        // Spring sẽ scan package com.lms và tự tạo beans cho:
        //   - LmsDataService  (@Service)
        //   - LmsUserApp      (@Component)
        // Sau đó inject LmsDataService vào LmsUserApp qua @Autowired
    }

    public static void main(String[] args) {
        // try-with-resources đảm bảo context được đóng đúng cách
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            LmsUserApp app = context.getBean(LmsUserApp.class);
            app.run();
        }
    }
}
