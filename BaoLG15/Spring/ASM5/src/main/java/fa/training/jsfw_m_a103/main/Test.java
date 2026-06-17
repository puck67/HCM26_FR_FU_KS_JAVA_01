package fa.training.jsfw_m_a103.main;

import fa.training.jsfw_m_a103.model.Instructor;
import fa.training.jsfw_m_a103.model.LmsUser;
import fa.training.jsfw_m_a103.model.Student;
import fa.training.jsfw_m_a103.service.LmsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Test {

    @Autowired
    private LmsDataService dataService;

    public void runDemo() {
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(1, "John Doe", "john.doe@fpt.com"));
        users.add(new Instructor(2, "Jane Expert", "jane.e@fpt.com", "Engineering", "Spring Framework Expert"));

        dataService.saveUsers(users);

        List<Student> students = new ArrayList<>();
        students.add(new Student(101, "Alice Smith", 3.75));
        students.add(new Student(102, "Bob Jones", 3.20));

        dataService.saveStudents(students);

        System.out.println("--- Demonstration of Polymorphism (LmsUsers/Instructors) ---");
        List<LmsUser> savedUsers = dataService.getUsers();
        for (LmsUser user : savedUsers) {
            user.printInfo();
        }

        System.out.println("\n--- Demonstration of Student List Retrieval ---");
        List<Student> savedStudents = dataService.getStudents();
        for (Student student : savedStudents) {
            student.printInfo();
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Test app = context.getBean(Test.class);
        app.runDemo();
    }
}