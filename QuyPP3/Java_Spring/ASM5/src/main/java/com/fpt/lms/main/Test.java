package com.fpt.lms.main;

import com.fpt.lms.model.Instructor;
import com.fpt.lms.model.LmsUser;
import com.fpt.lms.model.Student;
import com.fpt.lms.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println("==================================================");
        System.out.println("   INITIALIZING DATA & SAVING TO STORAGE          ");
        System.out.println("==================================================");

        List<LmsUser> users = Arrays.asList(
            new LmsUser(1L, "John Doe", "john.doe@fpt.com"),
            new Instructor(2L, "Jane Smith", "jane.smith@fpt.com", "Engineering", "Spring Framework Expert"),
            new Instructor(3L, "Bob Johnson", "bob.johnson@fpt.com", "Computer Science", "Java OOP Specialist")
        );
        dataService.saveUsers(users);

        List<Student> students = Arrays.asList(
            new Student(101L, "Alice Nguyen", 3.75),
            new Student(102L, "Bob Tran", 3.50),
            new Student(103L, "Charlie Le", 3.90)
        );
        dataService.saveStudents(students);

        System.out.println("\n==================================================");
        System.out.println("   RETRIEVING & PRINTING USERS (POLYMORPHISM)     ");
        System.out.println("==================================================");
        dataService.getUsers().forEach(LmsUser::printInfo);

        System.out.println("\n==================================================");
        System.out.println("   RETRIEVING & PRINTING STUDENTS                 ");
        System.out.println("==================================================");
        dataService.getStudents().forEach(Student::printInfo);
        
        System.out.println("==================================================");
    }

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            Test test = context.getBean(Test.class);
            test.run();
        }
    }
}

