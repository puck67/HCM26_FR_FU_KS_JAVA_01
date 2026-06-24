package com.example.demo.main;

import com.example.demo.model.Instructor;
import com.example.demo.model.LmsUser;
import com.example.demo.model.Student;
import com.example.demo.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Configuration
@ComponentScan(basePackages = {"com.example.demo.service", "com.example.demo.main"})
public class Test {

    @Autowired
    private LmsDataService dataService;

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Test.class)) {
            Test testInstance = context.getBean(Test.class);
            testInstance.runTasks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runTasks() {
        System.out.println("=== HAPPY PATHS ===");
        
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(1, "John Doe", "john.doe@fpt.com"));
        users.add(new Instructor(2, "Jane Doe", "jane.doe@fpt.com", "Engineering", "Spring Framework Expert"));

        dataService.saveUsers(users);

        List<Student> students = new ArrayList<>();
        students.add(new Student(101, "Jane Smith", 3.75));
        students.add(new Student(102, "Bob Johnson", 3.20));

        dataService.saveStudents(students);

        List<LmsUser> retrievedUsers = dataService.getUsers();
        if (retrievedUsers != null) {
            retrievedUsers.forEach(LmsUser::printInfo);
        }

        List<Student> retrievedStudents = dataService.getStudents();
        if (retrievedStudents != null) {
            retrievedStudents.forEach(Student::printInfo);
        }

        System.out.println("\n=== UNHAPPY PATHS & VALIDATION ===");

        try {
            List<LmsUser> invalidUsers = new ArrayList<>();
            invalidUsers.add(new LmsUser(3, null, "test@fpt.com"));
            dataService.saveUsers(invalidUsers);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception for null name: " + e.getMessage());
        }

        try {
            List<LmsUser> invalidUsers = new ArrayList<>();
            invalidUsers.add(new Instructor(4, "Invalid Email User", "not-an-email", "IT", "Bio"));
            dataService.saveUsers(invalidUsers);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception for invalid email: " + e.getMessage());
        }

        try {
            List<Student> invalidStudents = new ArrayList<>();
            invalidStudents.add(new Student(103, "High GPA Student", 4.5));
            dataService.saveStudents(invalidStudents);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception for GPA > 4.0: " + e.getMessage());
        }

        try {
            List<LmsUser> invalidUsers = new ArrayList<>();
            invalidUsers.add(new LmsUser(-1, "Negative ID User", "user@fpt.com"));
            dataService.saveUsers(invalidUsers);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception for negative ID: " + e.getMessage());
        }
    }
}
