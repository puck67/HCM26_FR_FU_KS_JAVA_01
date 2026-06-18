package fa.training.assignment5.main;

import fa.training.assignment5.config.AppConfig;
import fa.training.assignment5.model.Instructor;
import fa.training.assignment5.model.LmsUser;
import fa.training.assignment5.model.Student;
import fa.training.assignment5.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Test {

    @Autowired
    private LmsDataService dataService;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Test app = context.getBean(Test.class);
        app.run();
        context.close();
    }

    private void run() {
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(1L, "Alice Johnson", "alice.johnson@lms.com"));
        users.add(new Instructor(2L, "Robert Davis", "robert.davis@lms.com", "Computer Science", "Senior Java Instructor"));

        dataService.saveUsers(users);

        List<Student> students = new ArrayList<>();
        students.add(new Student(101L, "Charlie Brown", 3.85));
        students.add(new Student(102L, "David Wilson", 3.92));

        dataService.saveStudents(students);

        System.out.println("--- Saved Users ---");
        List<LmsUser> loadedUsers = dataService.getUsers();
        for (LmsUser u : loadedUsers) {
            u.printInfo();
        }

        System.out.println("\n--- Saved Students ---");
        List<Student> loadedStudents = dataService.getStudents();
        for (Student s : loadedStudents) {
            s.printInfo();
        }
    }
}
