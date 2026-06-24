package main;

import model.Instructor;
import model.LmsUser;
import model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import service.LmsDataService;

import java.util.Arrays;
import java.util.List;

@Component
public class Test {

    @Autowired
    LmsDataService dataService;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Test test = context.getBean(Test.class);

        List<LmsUser> users = Arrays.asList(
                new LmsUser(1L, "John Doe", "john.doe@fpt.com"),
                new Instructor(2L, "Jane Smith", "jane.smith@fpt.com", "Engineering", "Spring Framework Expert")
        );
        test.dataService.saveUsers(users);

        List<Student> students = Arrays.asList(
                new Student(101L, "Alice Johnson", 3.75),
                new Student(102L, "Bob Brown", 3.50)
        );
        test.dataService.saveStudents(students);

        List<LmsUser> loadedUsers = test.dataService.getUsers();
        loadedUsers.forEach(LmsUser::printInfo);

        List<Student> loadedStudents = test.dataService.getStudents();
        loadedStudents.forEach(Student::printInfo);
    }
}
