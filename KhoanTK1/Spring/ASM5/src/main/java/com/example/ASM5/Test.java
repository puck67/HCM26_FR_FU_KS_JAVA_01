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
    private LmsDataService lmsSrv;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Test.class);
        Test runner = ctx.getBean(Test.class);
        runner.run();
        ctx.close();
    }

    public void run() {
        LmsUser u = new LmsUser(1, "John Doe", "john.doe@fpt.com");
        Instructor inst = new Instructor(2, "Jane Smith", "jane.smith@fpt.com", "Engineering", "Spring Framework Expert");
        List<LmsUser> uList = Arrays.asList(u, inst);

        System.out.println("--- Saving Users ---");
        lmsSrv.saveUsers(uList);

        Student s1 = new Student(101, "Alice Cooper", 3.75);
        Student s2 = new Student(102, "Bob Marley", 3.20);
        List<Student> sList = Arrays.asList(s1, s2);

        System.out.println("--- Saving Students ---");
        lmsSrv.saveStudents(sList);

        System.out.println("\n--- Retrieved Users (Polymorphism) ---");
        List<LmsUser> readUsers = lmsSrv.getUsers();
        for (LmsUser userObj : readUsers) {
            userObj.printInfo();
        }

        System.out.println("\n--- Retrieved Students ---");
        List<Student> readStudents = lmsSrv.getStudents();
        for (Student stdObj : readStudents) {
            stdObj.printInfo();
        }
    }
}
