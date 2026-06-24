package com.lms.main;

import com.lms.model.Instructor;
import com.lms.model.LmsUser;
import com.lms.model.Student;
import com.lms.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Task 5: Test class - entry point của ứng dụng.
 *
 * Spring Annotation-based Configuration:
 *   @Component     → Spring tạo bean cho Test
 *   @Autowired     → Spring inject LmsDataService vào dataService (DI)
 *   @Service       → Spring tạo bean cho LmsDataService
 *   @ComponentScan → Spring tự scan package com.lms tìm tất cả bean
 */
@Component
public class Test {

    // Spring inject bean LmsDataService vào đây (Dependency Injection)
    @Autowired
    private LmsDataService dataService;

    public void run() {
        System.out.println("============================================================");
        System.out.println("         LMS - User Management System                       ");
        System.out.println("============================================================\n");

        // -------------------------------------------------------
        // Bước 1: Tạo và lưu danh sách LmsUser + Instructor
        // -------------------------------------------------------
        List<LmsUser> users = Arrays.asList(
            new LmsUser(1L, "Alice Nguyen", "alice@fpt.com"),
            new Instructor(2L, "John Doe",   "john.doe@fpt.com",   "Engineering",  "Spring Framework Expert"),
            new Instructor(3L, "Jane Smith", "jane.smith@fpt.com", "Data Science",  "Machine Learning Researcher"),
            new LmsUser(4L, "Bob Tran", "bob.tran@fpt.com")
        );

        System.out.println("--- [1] Saving users to " + "users.dat ---");
        dataService.saveUsers(users);

        // -------------------------------------------------------
        // Bước 2: Tạo và lưu danh sách Student
        // -------------------------------------------------------
        List<Student> students = Arrays.asList(
            new Student(101L, "Jane Smith",  3.75),
            new Student(102L, "Minh Le",     3.50),
            new Student(103L, "Huong Pham",  3.90),
            new Student(104L, "David Tran",  2.80)
        );

        System.out.println("\n--- [2] Saving students to students.dat ---");
        dataService.saveStudents(students);

        // -------------------------------------------------------
        // Bước 3: Đọc lại users và gọi printInfo() - thể hiện Polymorphism
        //         (Instructor.printInfo() vs LmsUser.printInfo())
        // -------------------------------------------------------
        System.out.println("\n--- [3] Loading & displaying users (Polymorphism demo) ---");
        List<LmsUser> loadedUsers = dataService.getUsers();
        for (LmsUser user : loadedUsers) {
            user.printInfo(); // Java tự gọi đúng phương thức theo kiểu thực tế
        }

        // -------------------------------------------------------
        // Bước 4: Đọc lại students và gọi printInfo()
        // -------------------------------------------------------
        System.out.println("\n--- [4] Loading & displaying students ---");
        List<Student> loadedStudents = dataService.getStudents();
        for (Student student : loadedStudents) {
            student.printInfo();
        }

        System.out.println("\n============================================================");
        System.out.println("   Done!                                                    ");
        System.out.println("============================================================");
    }

    // -----------------------------------------------------------
    // Spring Annotation-based Configuration (inner class)
    // -----------------------------------------------------------
    @Configuration
    @ComponentScan(basePackages = "com.lms")
    static class AppConfig {
        // Spring sẽ scan package com.lms và tự tạo beans cho:
        //   - LmsDataService  (@Service)
        //   - Test            (@Component)
        // Sau đó inject LmsDataService vào Test qua @Autowired
    }

    public static void main(String[] args) {
        // Khởi động Spring IoC Container
        ApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);

        // Lấy bean Test và chạy
        Test test = context.getBean(Test.class);
        test.run();

        ((AnnotationConfigApplicationContext) context).close();
    }
}
