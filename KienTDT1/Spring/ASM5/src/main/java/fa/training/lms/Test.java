package fa.training.lms;

import fa.training.lms.model.Instructor;
import fa.training.lms.model.LmsUser;
import fa.training.lms.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import fa.training.lms.service.LmsDataService;

import java.util.ArrayList;
import java.util.List;

@Component
public class Test implements CommandLineRunner {

    @Autowired
    private LmsDataService dataService;

    @Override
    public void run(String... args) {

        // User list
        List<LmsUser> users = new ArrayList<>();

        users.add(
                new LmsUser(
                        1L,
                        "John Doe",
                        "john@fpt.com"));

        users.add(
                new Instructor(
                        2L,
                        "David",
                        "david@fpt.com",
                        "Engineering",
                        "Spring Expert"));

        dataService.saveUsers(users);

        // Student list
        List<Student> students = new ArrayList<>();

        students.add(
                new Student(
                        101L,
                        "Jane Smith",
                        3.75));

        students.add(
                new Student(
                        102L,
                        "Tom",
                        3.5));

        dataService.saveStudents(students);

        // Read users
        System.out.println("=== USERS ===");

        List<LmsUser> savedUsers =
                dataService.getUsers();

        for (LmsUser user : savedUsers) {
            user.printInfo();   // polymorphism
        }

        // Read students
        System.out.println("=== STUDENTS ===");

        List<Student> savedStudents =
                dataService.getStudents();

        for (Student student : savedStudents) {
            student.printInfo();
        }
    }
}