package com.fpt.lms.main;

import com.fpt.lms.model.Instructor;
import com.fpt.lms.model.LmsUser;
import com.fpt.lms.model.Student;
import com.fpt.lms.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Test {

    @Autowired
    private LmsDataService dataService;

    public void run() {
        // Create and save LmsUser + Instructor list
        List<LmsUser> users = Arrays.asList(
            new LmsUser(1L, "John Doe", "john.doe@fpt.com"),
            new Instructor(2L, "Jane Smith", "jane.smith@fpt.com", "Engineering", "Spring Framework Expert"),
            new Instructor(3L, "Bob Johnson", "bob.johnson@fpt.com", "Computer Science", "Java OOP Specialist")
        );
        dataService.saveUsers(users);

        // Create and save Students list
        List<Student> students = Arrays.asList(
            new Student(101L, "Alice Nguyen", 3.75),
            new Student(102L, "Bob Tran", 3.50),
            new Student(103L, "Charlie Le", 3.90)
        );
        dataService.saveStudents(students);

        // Load and display users (polymorphism)
        System.out.println("\n--- Loaded Users ---");
        List<LmsUser> loadedUsers = dataService.getUsers();
        for (LmsUser u : loadedUsers) {
            u.printInfo();
        }

        // Load and display students
        System.out.println("\n--- Loaded Students ---");
        List<Student> loadedStudents = dataService.getStudents();
        for (Student s : loadedStudents) {
            s.printInfo();
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Test test = context.getBean(Test.class);
        test.run();
    }
}
