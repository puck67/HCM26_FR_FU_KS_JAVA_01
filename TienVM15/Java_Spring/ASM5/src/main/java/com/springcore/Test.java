package com.springcore;

import com.springcore.config.AppConfig;
import com.springcore.model.Instructor;
import com.springcore.model.LmsUser;
import com.springcore.model.Student;
import com.springcore.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Test {

    private final LmsDataService dataService;

    @Autowired
    public Test(LmsDataService dataService) {
        this.dataService = dataService;
    }

    public void run() {
        // 1. Create a list containing LmsUser and Instructor objects
        List<LmsUser> users = Arrays.asList(
                new LmsUser(1, "John Doe", "john.doe@fpt.com"),
                new Instructor(2, "Alice Vance", "alice.vance@fpt.com", "Engineering", "Spring Framework Expert"),
                new LmsUser(3, "Bob Miller", "bob.miller@fpt.com"),
                new Instructor(4, "Charlie Brown", "charlie.brown@fpt.com", "Science", "Java OOP Expert")
        );

        // 2. Save users to users.dat
        System.out.println("--- Saving LmsUsers & Instructors ---");
        dataService.saveUsers(users);

        // 3. Create a list of Student objects
        List<Student> students = Arrays.asList(
                new Student(101, "Jane Smith", 3.75),
                new Student(102, "David Lee", 3.20),
                new Student(103, "Emma Watson", 3.90)
        );

        // 4. Save students to students.dat
        System.out.println("\n--- Saving Students ---");
        dataService.saveStudents(students);

        // 5. Retrieve saved LmsUser list from users.dat and call printInfo() (polymorphism)
        System.out.println("\n--- Loading & Displaying LmsUsers (Polymorphism) ---");
        List<LmsUser> retrievedUsers = dataService.getUsers();
        for (LmsUser user : retrievedUsers) {
            user.printInfo();
        }

        // 6. Retrieve saved Student list from students.dat and call printInfo()
        System.out.println("\n--- Loading & Displaying Students ---");
        List<Student> retrievedStudents = dataService.getStudents();
        for (Student student : retrievedStudents) {
            student.printInfo();
        }
    }

    public static void main(String[] args) {
        // Create context using AppConfig config class
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the Test bean and run the flow
        Test test = context.getBean(Test.class);
        test.run();
    }
}
