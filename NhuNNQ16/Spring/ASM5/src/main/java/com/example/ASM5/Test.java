package com.example.ASM5;

import com.example.ASM5.model.Instructor;
import com.example.ASM5.model.LmsUser;
import com.example.ASM5.model.Student;
import com.example.ASM5.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.example.ASM5")
public class Test {

    @Autowired
    private LmsDataService dataService;

    public static void main(String[] args) {
        // Initialize the Annotation-based Spring context
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Test.class);

        // Retrieve the Test configuration bean which has the dataService injected
        Test testApp = context.getBean(Test.class);

        // Run the serialization and deserialization demo
        testApp.run();

        // Close the application context
        context.close();
    }

    public void run() {
        // 1. Create a list containing LmsUser and Instructor objects and save it to the file users.dat
        LmsUser user = new LmsUser(1, "John Doe", "john.doe@fpt.com");
        Instructor instructor = new Instructor(2, "Jane Smith", "jane.smith@fpt.com", "Engineering", "Spring Framework Expert");
        List<LmsUser> users = Arrays.asList(user, instructor);

        System.out.println("--- Saving Users ---");
        dataService.saveUsers(users);

        // 2. Create a list of Student objects and save it to the file students.dat
        Student student1 = new Student(101, "Alice Cooper", 3.75);
        Student student2 = new Student(102, "Bob Marley", 3.20);
        List<Student> students = Arrays.asList(student1, student2);

        System.out.println("--- Saving Students ---");
        dataService.saveStudents(students);

        // 3. Retrieve the saved list of LmsUser objects from users.dat and call printInfo() (Polymorphism)
        System.out.println("\n--- Retrieved Users (Polymorphism) ---");
        List<LmsUser> retrievedUsers = dataService.getUsers();
        for (LmsUser u : retrievedUsers) {
            u.printInfo();
        }

        // 4. Retrieve the saved list of Student objects from students.dat and call printInfo()
        System.out.println("\n--- Retrieved Students ---");
        List<Student> retrievedStudents = dataService.getStudents();
        for (Student s : retrievedStudents) {
            s.printInfo();
        }
    }
}
