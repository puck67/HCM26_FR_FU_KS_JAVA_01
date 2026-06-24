package com.example.asm5;

import com.example.asm5.model.Instructor;
import com.example.asm5.model.LmsUser;
import com.example.asm5.model.Student;
import com.example.asm5.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Test implements CommandLineRunner {

    // Attribute LmsDataService dataService for handling file operations
    private final LmsDataService dataService;

    // Injecting the LmsDataService bean using Spring Constructor Injection
    @Autowired
    public Test(LmsDataService dataService) {
        this.dataService = dataService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Create a list containing LmsUser and Instructor objects and save it to the file users.dat
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(1, "John Doe", "john.doe@fpt.com"));
        users.add(new Instructor(2, "Jane Doe", "jane.doe@fpt.com", "Engineering", "Spring Framework Expert"));

        System.out.println("--- Saving Users to users.dat ---");
        dataService.saveUsers(users);

        // 2. Create a list of Student objects and save it to the file students.dat
        List<Student> students = new ArrayList<>();
        students.add(new Student(101, "Jane Smith", 3.75));
        students.add(new Student(102, "Bob Miller", 3.40));

        System.out.println("--- Saving Students to students.dat ---");
        dataService.saveStudents(students);

        // 3. Retrieve the saved list of LmsUser objects from users.dat and call printInfo() (demonstrating polymorphism)
        System.out.println("\n--- Displaying Loaded Users (Polymorphism) ---");
        List<LmsUser> retrievedUsers = dataService.getUsers();
        for (LmsUser user : retrievedUsers) {
            user.printInfo();
        }

        // 4. Retrieve the saved list of Student objects from students.dat and call printInfo()
        System.out.println("\n--- Displaying Loaded Students ---");
        List<Student> retrievedStudents = dataService.getStudents();
        for (Student student : retrievedStudents) {
            student.printInfo();
        }
    }
}
