package com.example.Ex2.config;

import com.example.Ex2.model.Menu;
import com.example.Ex2.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if empty
        if (menuRepository.count() == 0) {
            // 1. Dashboard
            Menu dashboard = new Menu();
            dashboard.setName("Dashboard");
            dashboard.setUrl("/dashboard");
            dashboard.setIcon("bi bi-speedometer2");
            dashboard.setDisplayOrder(1);
            dashboard.setStatus(true);
            dashboard.setRoles("ADMIN,TEACHER,STUDENT");
            menuRepository.save(dashboard);

            // 2. User Management (Parent)
            Menu userMgmt = new Menu();
            userMgmt.setName("User Management");
            userMgmt.setUrl(null);
            userMgmt.setIcon("bi bi-people");
            userMgmt.setDisplayOrder(2);
            userMgmt.setStatus(true);
            userMgmt.setRoles("ADMIN");
            menuRepository.save(userMgmt);

            // 2.1 Student Management (Child of 2)
            Menu studentMgmt = new Menu();
            studentMgmt.setName("Student Management");
            studentMgmt.setUrl("/students");
            studentMgmt.setIcon("bi bi-mortarboard");
            studentMgmt.setParent(userMgmt);
            studentMgmt.setDisplayOrder(1);
            studentMgmt.setStatus(true);
            studentMgmt.setRoles("ADMIN");
            menuRepository.save(studentMgmt);

            // 2.2 Lecturer Management (Child of 2)
            Menu lecturerMgmt = new Menu();
            lecturerMgmt.setName("Lecturer Management");
            lecturerMgmt.setUrl("/lecturers");
            lecturerMgmt.setIcon("bi bi-person-badge");
            lecturerMgmt.setParent(userMgmt);
            lecturerMgmt.setDisplayOrder(2);
            lecturerMgmt.setStatus(true);
            lecturerMgmt.setRoles("ADMIN");
            menuRepository.save(lecturerMgmt);

            // 3. Training Management (Parent)
            Menu trainingMgmt = new Menu();
            trainingMgmt.setName("Training Management");
            trainingMgmt.setUrl(null);
            trainingMgmt.setIcon("bi bi-book");
            trainingMgmt.setDisplayOrder(3);
            trainingMgmt.setStatus(true);
            trainingMgmt.setRoles("ADMIN,TEACHER");
            menuRepository.save(trainingMgmt);

            // 3.1 Subject Management (Child of 3)
            Menu subjectMgmt = new Menu();
            subjectMgmt.setName("Subject Management");
            subjectMgmt.setUrl("/subjects");
            subjectMgmt.setIcon("bi bi-journal-text");
            subjectMgmt.setParent(trainingMgmt);
            subjectMgmt.setDisplayOrder(1);
            subjectMgmt.setStatus(true);
            subjectMgmt.setRoles("ADMIN,TEACHER");
            menuRepository.save(subjectMgmt);

            // 3.2 Course Management (Child of 3 - Parent of 3rd level)
            Menu courseMgmt = new Menu();
            courseMgmt.setName("Course Management");
            courseMgmt.setUrl("/courses");
            courseMgmt.setIcon("bi bi-calendar-event");
            courseMgmt.setParent(trainingMgmt);
            courseMgmt.setDisplayOrder(2);
            courseMgmt.setStatus(true);
            courseMgmt.setRoles("ADMIN,TEACHER");
            menuRepository.save(courseMgmt);

            // 3.2.1 Online Courses (Child of 3.2 - 3rd level)
            Menu onlineCourses = new Menu();
            onlineCourses.setName("Online Courses");
            onlineCourses.setUrl("/courses/online");
            onlineCourses.setIcon("bi bi-laptop");
            onlineCourses.setParent(courseMgmt);
            onlineCourses.setDisplayOrder(1);
            onlineCourses.setStatus(true);
            onlineCourses.setRoles("ADMIN,TEACHER,STUDENT");
            menuRepository.save(onlineCourses);

            // 3.2.2 Offline Courses (Child of 3.2 - 3rd level)
            Menu offlineCourses = new Menu();
            offlineCourses.setName("Offline Courses");
            offlineCourses.setUrl("/courses/offline");
            offlineCourses.setIcon("bi bi-house-door");
            offlineCourses.setParent(courseMgmt);
            offlineCourses.setDisplayOrder(2);
            offlineCourses.setStatus(true);
            offlineCourses.setRoles("ADMIN,TEACHER,STUDENT");
            menuRepository.save(offlineCourses);

            // 4. Exam Management (Parent)
            Menu examMgmt = new Menu();
            examMgmt.setName("Exam Management");
            examMgmt.setUrl(null);
            examMgmt.setIcon("bi bi-file-earmark-text");
            examMgmt.setDisplayOrder(4);
            examMgmt.setStatus(true);
            examMgmt.setRoles("ADMIN,TEACHER");
            menuRepository.save(examMgmt);

            // 4.1 Question Bank
            Menu questionBank = new Menu();
            questionBank.setName("Question Bank");
            questionBank.setUrl("/questions");
            questionBank.setIcon("bi bi-database");
            questionBank.setParent(examMgmt);
            questionBank.setDisplayOrder(1);
            questionBank.setStatus(true);
            questionBank.setRoles("ADMIN,TEACHER");
            menuRepository.save(questionBank);

            // 4.2 Exam
            Menu exam = new Menu();
            exam.setName("Exam");
            exam.setUrl("/exams");
            exam.setIcon("bi bi-pencil-square");
            exam.setParent(examMgmt);
            exam.setDisplayOrder(2);
            exam.setStatus(true);
            exam.setRoles("ADMIN,TEACHER");
            menuRepository.save(exam);

            // 4.3 Result
            Menu result = new Menu();
            result.setName("Result");
            result.setUrl("/results");
            result.setIcon("bi bi-trophy");
            result.setParent(examMgmt);
            result.setDisplayOrder(3);
            result.setStatus(true);
            result.setRoles("ADMIN,TEACHER,STUDENT");
            menuRepository.save(result);

            // 5. System Management (Parent)
            Menu systemMgmt = new Menu();
            systemMgmt.setName("System Management");
            systemMgmt.setUrl(null);
            systemMgmt.setIcon("bi bi-gear");
            systemMgmt.setDisplayOrder(5);
            systemMgmt.setStatus(true);
            systemMgmt.setRoles("ADMIN");
            menuRepository.save(systemMgmt);

            // 5.1 Roles
            Menu roles = new Menu();
            roles.setName("Roles");
            roles.setUrl("/roles");
            roles.setIcon("bi bi-shield-lock");
            roles.setParent(systemMgmt);
            roles.setDisplayOrder(1);
            roles.setStatus(true);
            roles.setRoles("ADMIN");
            menuRepository.save(roles);

            // 5.2 Users
            Menu users = new Menu();
            users.setName("Users");
            users.setUrl("/users");
            users.setIcon("bi bi-person-bounding-box");
            users.setParent(systemMgmt);
            users.setDisplayOrder(2);
            users.setStatus(true);
            users.setRoles("ADMIN");
            menuRepository.save(users);

            System.out.println("Database seeding completed. " + menuRepository.count() + " menu items created.");
        }
    }
}
