package com.example.lms.config;

import com.example.lms.entity.Course;
import com.example.lms.entity.CourseId;
import com.example.lms.entity.Menu;
import com.example.lms.entity.Student;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.MenuRepository;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final MenuRepository menuRepository;

    @Override
    public void run(String... args) throws Exception {
        if (menuRepository.count() == 0) {
            menuRepository.save(createMenu("Dashboard", "/dashboard", "fa-solid fa-chart-pie", "ROLE_ADMIN", 1));
            menuRepository.save(createMenu("Students", "/students", "fa-solid fa-users", "ROLE_ADMIN", 2));
            menuRepository.save(createMenu("Courses", "/courses", "fa-solid fa-book", "ROLE_ADMIN", 3));
            menuRepository.save(createMenu("Menus", "/menus", "fa-solid fa-bars", "ROLE_ADMIN", 4));
            log.info("Default role-based menus seeded.");
        }

        if (studentRepository.count() == 0) {
            for (int i = 1; i <= 25; i++) {
                Student student = new Student();
                student.setStudentCode(String.format("S%05d", i));
                student.setFullName("Student Name " + i);
                student.setEmail("student" + i + "@example.com");
                studentRepository.save(student);
            }
            log.info("Default student data seeded.");
        }

        if (courseRepository.count() == 0) {
            courseRepository.save(new Course(new CourseId("C001", LocalDate.now()), "Spring Boot Masterclass", "Programming", "John Doe"));
            courseRepository.save(new Course(new CourseId("C002", LocalDate.now().plusDays(1)), "React JS UI Development", "Web Design", "Jane Smith"));
            courseRepository.save(new Course(new CourseId("C003", LocalDate.now().plusDays(2)), "Python for Data Science", "Data Science", "Alan Turing"));
            courseRepository.save(new Course(new CourseId("C004", LocalDate.now().plusDays(3)), "Database Administration", "Database", "Ada Lovelace"));
            log.info("Default course data seeded.");
        }
    }

    private Menu createMenu(String title, String url, String icon, String role, int orderIndex) {
        Menu m = new Menu();
        m.setTitle(title);
        m.setUrl(url);
        m.setIcon(icon);
        m.setRole(role);
        m.setOrderIndex(orderIndex);
        return m;
    }
}
