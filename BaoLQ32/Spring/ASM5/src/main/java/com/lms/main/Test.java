package com.lms.main;

import com.lms.model.Instructor;
import com.lms.model.LmsUser;
import com.lms.model.Student;
import com.lms.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Test {

    @Autowired
    private LmsDataService dataService;

    public static void main(String[] args) {
        // Initialize Spring container
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the Test bean to trigger DI and run operations
        Test testApp = context.getBean(Test.class);
        testApp.run();
    }

    public void run() {
        // 1. Create a list containing LmsUser and Instructor objects and save it
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(1, "John Doe", "john.doe@fpt.com"));
        users.add(new Instructor(2, "Alice Smith", "alice.smith@fpt.com", "Computer Science", "Spring Core expert with 10 years of experience."));
        users.add(new LmsUser(3, "Bob Johnson", "bob.johnson@fpt.com"));
        
        System.out.println("Saving users to users.dat...");
        dataService.saveUsers(users);

        // 2. Create a list of Student objects and save it
        List<Student> students = new ArrayList<>();
        students.add(new Student(101, "Jane Smith", 3.75));
        students.add(new Student(102, "Tom Davis", 3.20));

        System.out.println("Saving students to students.dat...");
        dataService.saveStudents(students);

        // 3. Retrieve saved users and print their info (demonstrates polymorphism)
        System.out.println("\nRetrieving users from users.dat and displaying details:");
        List<LmsUser> retrievedUsers = dataService.getUsers();
        for (LmsUser user : retrievedUsers) {
            user.printInfo();
        }

        // 4. Retrieve saved students and print their info
        System.out.println("\nRetrieving students from students.dat and displaying details:");
        List<Student> retrievedStudents = dataService.getStudents();
        for (Student student : retrievedStudents) {
            student.printInfo();
        }
    }
}
