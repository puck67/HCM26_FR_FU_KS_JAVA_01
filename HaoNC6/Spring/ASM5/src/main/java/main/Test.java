package main;

import config.AppConfig;
import model.Instructor;
import model.LmsUser;
import model.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.LmsDataService;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private LmsDataService dataService;

    public void setDataService(LmsDataService dataService) {
        this.dataService = dataService;
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        Test test = new Test();
        // Inject the bean into the dataService attribute
        test.setDataService(context.getBean(LmsDataService.class));

        // 1. Create a list containing LmsUser and Instructor objects and save it to the file users.dat
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(1, "John Doe", "john.doe@fpt.com"));
        users.add(new Instructor(2, "Jane Smith", "jane.smith@fpt.com", "Engineering", "Spring Framework Expert"));
        test.dataService.saveUsers(users);

        // 2. Create a list of Student objects and save it to the file students.dat
        List<Student> students = new ArrayList<>();
        students.add(new Student(101, "Alice Williams", 3.75));
        students.add(new Student(102, "Bob Brown", 3.9));
        test.dataService.saveStudents(students);

        // 3. Retrieve the saved list of LmsUser objects from users.dat and call printInfo() for each object
        System.out.println("--- Saved Users ---");
        List<LmsUser> retrievedUsers = test.dataService.getUsers();
        for (LmsUser user : retrievedUsers) {
            user.printInfo();
        }

        // 4. Retrieve the saved list of Student objects from students.dat and call printInfo() for each object
        System.out.println("\n--- Saved Students ---");
        List<Student> retrievedStudents = test.dataService.getStudents();
        for (Student student : retrievedStudents) {
            student.printInfo();
        }
    }
}
