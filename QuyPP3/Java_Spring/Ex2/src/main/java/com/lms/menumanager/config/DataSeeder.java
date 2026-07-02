package com.lms.menumanager.config;

import com.lms.menumanager.entity.Menu;
import com.lms.menumanager.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MenuRepository menuRepository;

    public DataSeeder(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (menuRepository.count() == 0) {
            seedMenus();
        }
    }

    private void seedMenus() {
        // 1. Dashboard (All roles)
        Menu dashboard = new Menu()
                .setName("Dashboard")
                .setUrl("/")
                .setIcon("bi bi-speedometer2")
                .setDisplayOrder(1)
                .setStatus(true)
                .setRoles("ADMIN,TEACHER,STUDENT");
        menuRepository.save(dashboard);

        // 2. User Management (Admin only)
        Menu userMgmt = new Menu()
                .setName("User Management")
                .setUrl("")
                .setIcon("bi bi-people-fill")
                .setDisplayOrder(2)
                .setStatus(true)
                .setRoles("ADMIN");
        menuRepository.save(userMgmt);

        // 2.1 Student Management
        Menu studentMgmt = new Menu()
                .setName("Student Management")
                .setUrl("/students")
                .setIcon("bi bi-mortarboard-fill")
                .setDisplayOrder(1)
                .setStatus(true)
                .setRoles("ADMIN")
                .setParent(userMgmt);
        menuRepository.save(studentMgmt);

        // 2.2 Lecturer Management
        Menu lecturerMgmt = new Menu()
                .setName("Lecturer Management")
                .setUrl("/lecturers")
                .setIcon("bi bi-person-badge-fill")
                .setDisplayOrder(2)
                .setStatus(true)
                .setRoles("ADMIN")
                .setParent(userMgmt);
        menuRepository.save(lecturerMgmt);

        // 3. Training Management (Admin, Teacher, Student)
        Menu trainingMgmt = new Menu()
                .setName("Training Management")
                .setUrl("")
                .setIcon("bi bi-journal-album")
                .setDisplayOrder(3)
                .setStatus(true)
                .setRoles("ADMIN,TEACHER,STUDENT");
        menuRepository.save(trainingMgmt);

        // 3.1 Subject Management
        Menu subjectMgmt = new Menu()
                .setName("Subject Management")
                .setUrl("/subjects")
                .setIcon("bi bi-book-half")
                .setDisplayOrder(1)
                .setStatus(true)
                .setRoles("ADMIN,TEACHER")
                .setParent(trainingMgmt);
        menuRepository.save(subjectMgmt);

        // 3.2 Course Management
        Menu courseMgmt = new Menu()
                .setName("Course Management")
                .setUrl("/courses")
                .setIcon("bi bi-collection-play-fill")
                .setDisplayOrder(2)
                .setStatus(true)
                .setRoles("ADMIN,TEACHER")
                .setParent(trainingMgmt);
        menuRepository.save(courseMgmt);

        // 3.2.1 Online Courses (Level 3 Bonus)
        Menu onlineCourses = new Menu()
                .setName("Online Courses")
                .setUrl("/courses/online")
                .setIcon("bi bi-globe2")
                .setDisplayOrder(1)
                .setStatus(true)
                .setRoles("ADMIN,TEACHER")
                .setParent(courseMgmt);
        menuRepository.save(onlineCourses);

        // 3.2.2 Offline Courses (Level 3 Bonus)
        Menu offlineCourses = new Menu()
                .setName("Offline Courses")
                .setUrl("/courses/offline")
                .setIcon("bi bi-building")
                .setDisplayOrder(2)
                .setStatus(true)
                .setRoles("ADMIN,TEACHER")
                .setParent(courseMgmt);
        menuRepository.save(offlineCourses);

        // 4. My Courses (Student only - separate root or student course access)
        Menu myCourses = new Menu()
                .setName("My Courses")
                .setUrl("/my-courses")
                .setIcon("bi bi-journal-check")
                .setDisplayOrder(4)
                .setStatus(true)
                .setRoles("STUDENT");
        menuRepository.save(myCourses);

        // 5. Exam Management (Admin and Teacher)
        Menu examMgmt = new Menu()
                .setName("Exam Management")
                .setUrl("")
                .setIcon("bi bi-file-earmark-text-fill")
                .setDisplayOrder(5)
                .setStatus(true)
                .setRoles("ADMIN,TEACHER");
        menuRepository.save(examMgmt);

        // 5.1 Question Bank
        Menu questionBank = new Menu()
                .setName("Question Bank")
                .setUrl("/questions")
                .setIcon("bi bi-question-diamond-fill")
                .setDisplayOrder(1)
                .setStatus(true)
                .setRoles("ADMIN,TEACHER")
                .setParent(examMgmt);
        menuRepository.save(questionBank);

        // 5.2 Exam
        Menu exam = new Menu()
                .setName("Exam")
                .setUrl("/exams")
                .setIcon("bi bi-pencil-square")
                .setDisplayOrder(2)
                .setStatus(true)
                .setRoles("ADMIN,TEACHER")
                .setParent(examMgmt);
        menuRepository.save(exam);

        // 5.3 Result
        Menu result = new Menu()
                .setName("Result")
                .setUrl("/results")
                .setIcon("bi bi-clipboard-data-fill")
                .setDisplayOrder(3)
                .setStatus(true)
                .setRoles("ADMIN,TEACHER,STUDENT")
                .setParent(examMgmt);
        menuRepository.save(result);

        // 6. System Management (Admin only)
        Menu systemMgmt = new Menu()
                .setName("System Management")
                .setUrl("")
                .setIcon("bi bi-gear-fill")
                .setDisplayOrder(6)
                .setStatus(true)
                .setRoles("ADMIN");
        menuRepository.save(systemMgmt);

        // 6.1 Roles
        Menu rolesMenu = new Menu()
                .setName("Roles")
                .setUrl("/roles")
                .setIcon("bi bi-shield-lock-fill")
                .setDisplayOrder(1)
                .setStatus(true)
                .setRoles("ADMIN")
                .setParent(systemMgmt);
        menuRepository.save(rolesMenu);

        // 6.2 Users
        Menu usersMenu = new Menu()
                .setName("Users")
                .setUrl("/users")
                .setIcon("bi bi-person-gear")
                .setDisplayOrder(2)
                .setStatus(true)
                .setRoles("ADMIN")
                .setParent(systemMgmt);
        menuRepository.save(usersMenu);
    }
}
