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

    @Autowired
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
        Menu dashboard = new Menu();
        dashboard.setName("Dashboard");
        dashboard.setUrl("/");
        dashboard.setIcon("bi bi-speedometer2");
        dashboard.setDisplayOrder(1);
        dashboard.setStatus(true);
        dashboard.setRoles("ADMIN,TEACHER,STUDENT");
        menuRepository.save(dashboard);

        // 2. User Management (Admin only)
        Menu userMgmt = new Menu();
        userMgmt.setName("User Management");
        userMgmt.setUrl("");
        userMgmt.setIcon("bi bi-people-fill");
        userMgmt.setDisplayOrder(2);
        userMgmt.setStatus(true);
        userMgmt.setRoles("ADMIN");
        menuRepository.save(userMgmt);

        // 2.1 Student Management
        Menu studentMgmt = new Menu();
        studentMgmt.setName("Student Management");
        studentMgmt.setUrl("/students");
        studentMgmt.setIcon("bi bi-mortarboard-fill");
        studentMgmt.setDisplayOrder(1);
        studentMgmt.setStatus(true);
        studentMgmt.setRoles("ADMIN");
        studentMgmt.setParent(userMgmt);
        menuRepository.save(studentMgmt);

        // 2.2 Lecturer Management
        Menu lecturerMgmt = new Menu();
        lecturerMgmt.setName("Lecturer Management");
        lecturerMgmt.setUrl("/lecturers");
        lecturerMgmt.setIcon("bi bi-person-badge-fill");
        lecturerMgmt.setDisplayOrder(2);
        lecturerMgmt.setStatus(true);
        lecturerMgmt.setRoles("ADMIN");
        lecturerMgmt.setParent(userMgmt);
        menuRepository.save(lecturerMgmt);

        // 3. Training Management (Admin, Teacher, Student)
        Menu trainingMgmt = new Menu();
        trainingMgmt.setName("Training Management");
        trainingMgmt.setUrl("");
        trainingMgmt.setIcon("bi bi-journal-album");
        trainingMgmt.setDisplayOrder(3);
        trainingMgmt.setStatus(true);
        trainingMgmt.setRoles("ADMIN,TEACHER,STUDENT");
        menuRepository.save(trainingMgmt);

        // 3.1 Subject Management
        Menu subjectMgmt = new Menu();
        subjectMgmt.setName("Subject Management");
        subjectMgmt.setUrl("/subjects");
        subjectMgmt.setIcon("bi bi-book-half");
        subjectMgmt.setDisplayOrder(1);
        subjectMgmt.setStatus(true);
        subjectMgmt.setRoles("ADMIN,TEACHER");
        subjectMgmt.setParent(trainingMgmt);
        menuRepository.save(subjectMgmt);

        // 3.2 Course Management
        Menu courseMgmt = new Menu();
        courseMgmt.setName("Course Management");
        courseMgmt.setUrl("/courses");
        courseMgmt.setIcon("bi bi-collection-play-fill");
        courseMgmt.setDisplayOrder(2);
        courseMgmt.setStatus(true);
        courseMgmt.setRoles("ADMIN,TEACHER");
        courseMgmt.setParent(trainingMgmt);
        menuRepository.save(courseMgmt);

        // 3.2.1 Online Courses (Level 3 Bonus)
        Menu onlineCourses = new Menu();
        onlineCourses.setName("Online Courses");
        onlineCourses.setUrl("/courses/online");
        onlineCourses.setIcon("bi bi-globe2");
        onlineCourses.setDisplayOrder(1);
        onlineCourses.setStatus(true);
        onlineCourses.setRoles("ADMIN,TEACHER");
        onlineCourses.setParent(courseMgmt);
        menuRepository.save(onlineCourses);

        // 3.2.2 Offline Courses (Level 3 Bonus)
        Menu offlineCourses = new Menu();
        offlineCourses.setName("Offline Courses");
        offlineCourses.setUrl("/courses/offline");
        offlineCourses.setIcon("bi bi-building");
        offlineCourses.setDisplayOrder(2);
        offlineCourses.setStatus(true);
        offlineCourses.setRoles("ADMIN,TEACHER");
        offlineCourses.setParent(courseMgmt);
        menuRepository.save(offlineCourses);

        // 4. My Courses (Student only - separate root or student course access)
        Menu myCourses = new Menu();
        myCourses.setName("My Courses");
        myCourses.setUrl("/my-courses");
        myCourses.setIcon("bi bi-journal-check");
        myCourses.setDisplayOrder(4);
        myCourses.setStatus(true);
        myCourses.setRoles("STUDENT");
        menuRepository.save(myCourses);

        // 5. Exam Management (Admin and Teacher)
        Menu examMgmt = new Menu();
        examMgmt.setName("Exam Management");
        examMgmt.setUrl("");
        examMgmt.setIcon("bi bi-file-earmark-text-fill");
        examMgmt.setDisplayOrder(5);
        examMgmt.setStatus(true);
        examMgmt.setRoles("ADMIN,TEACHER");
        menuRepository.save(examMgmt);

        // 5.1 Question Bank
        Menu questionBank = new Menu();
        questionBank.setName("Question Bank");
        questionBank.setUrl("/questions");
        questionBank.setIcon("bi bi-question-diamond-fill");
        questionBank.setDisplayOrder(1);
        questionBank.setStatus(true);
        questionBank.setRoles("ADMIN,TEACHER");
        questionBank.setParent(examMgmt);
        menuRepository.save(questionBank);

        // 5.2 Exam
        Menu exam = new Menu();
        exam.setName("Exam");
        exam.setUrl("/exams");
        exam.setIcon("bi bi-pencil-square");
        exam.setDisplayOrder(2);
        exam.setStatus(true);
        exam.setRoles("ADMIN,TEACHER");
        exam.setParent(examMgmt);
        menuRepository.save(exam);

        // 5.3 Result
        Menu result = new Menu();
        result.setName("Result");
        result.setUrl("/results");
        result.setIcon("bi bi-clipboard-data-fill");
        result.setDisplayOrder(3);
        result.setStatus(true);
        result.setRoles("ADMIN,TEACHER,STUDENT");
        result.setParent(examMgmt);
        menuRepository.save(result);

        // 6. System Management (Admin only)
        Menu systemMgmt = new Menu();
        systemMgmt.setName("System Management");
        systemMgmt.setUrl("");
        systemMgmt.setIcon("bi bi-gear-fill");
        systemMgmt.setDisplayOrder(6);
        systemMgmt.setStatus(true);
        systemMgmt.setRoles("ADMIN");
        menuRepository.save(systemMgmt);

        // 6.1 Roles
        Menu rolesMenu = new Menu();
        rolesMenu.setName("Roles");
        rolesMenu.setUrl("/roles");
        rolesMenu.setIcon("bi bi-shield-lock-fill");
        rolesMenu.setDisplayOrder(1);
        rolesMenu.setStatus(true);
        rolesMenu.setRoles("ADMIN");
        rolesMenu.setParent(systemMgmt);
        menuRepository.save(rolesMenu);

        // 6.2 Users
        Menu usersMenu = new Menu();
        usersMenu.setName("Users");
        usersMenu.setUrl("/users");
        usersMenu.setIcon("bi bi-person-gear");
        usersMenu.setDisplayOrder(2);
        usersMenu.setStatus(true);
        usersMenu.setRoles("ADMIN");
        usersMenu.setParent(systemMgmt);
        menuRepository.save(usersMenu);
    }
}
