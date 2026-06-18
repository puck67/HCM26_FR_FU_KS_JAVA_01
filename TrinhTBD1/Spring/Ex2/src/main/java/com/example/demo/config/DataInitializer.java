package com.example.demo.config;

import com.example.demo.model.Menu;
import com.example.demo.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final MenuRepository menuRepository;

    public DataInitializer(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void run(String... args) {
        if (menuRepository.count() == 0) {
            log.info("Database is empty. Seeding initial menu data...");

            Menu dashboard = Menu.builder()
                    .name("Dashboard")
                    .url("/dashboard")
                    .icon("bi-speedometer2")
                    .displayOrder(1)
                    .status(true)
                    .roles("ADMIN,TEACHER,STUDENT")
                    .children(new ArrayList<>())
                    .build();
            menuRepository.save(dashboard);

            Menu userMgmt = Menu.builder()
                    .name("User Management")
                    .url("")
                    .icon("bi-people")
                    .displayOrder(2)
                    .status(true)
                    .roles("ADMIN")
                    .children(new ArrayList<>())
                    .build();
            userMgmt = menuRepository.save(userMgmt);

            Menu studentMgmt = Menu.builder()
                    .name("Student Management")
                    .url("/students")
                    .icon("bi-person-badge")
                    .displayOrder(1)
                    .status(true)
                    .roles("ADMIN")
                    .parent(userMgmt)
                    .children(new ArrayList<>())
                    .build();
            menuRepository.save(studentMgmt);

            Menu lecturerMgmt = Menu.builder()
                    .name("Lecturer Management")
                    .url("/lecturers")
                    .icon("bi-person-workspace")
                    .displayOrder(2)
                    .status(true)
                    .roles("ADMIN")
                    .parent(userMgmt)
                    .children(new ArrayList<>())
                    .build();
            menuRepository.save(lecturerMgmt);

            Menu trainingMgmt = Menu.builder()
                    .name("Training Management")
                    .url("")
                    .icon("bi-journal-bookmark")
                    .displayOrder(3)
                    .status(true)
                    .roles("ADMIN,TEACHER")
                    .children(new ArrayList<>())
                    .build();
            trainingMgmt = menuRepository.save(trainingMgmt);

            Menu subjectMgmt = Menu.builder()
                    .name("Subject Management")
                    .url("/subjects")
                    .icon("bi-book")
                    .displayOrder(1)
                    .status(true)
                    .roles("ADMIN")
                    .parent(trainingMgmt)
                    .children(new ArrayList<>())
                    .build();
            menuRepository.save(subjectMgmt);

            Menu courseMgmt = Menu.builder()
                    .name("Course Management")
                    .url("/courses")
                    .icon("bi-laptop")
                    .displayOrder(2)
                    .status(true)
                    .roles("ADMIN,TEACHER")
                    .parent(trainingMgmt)
                    .children(new ArrayList<>())
                    .build();
            courseMgmt = menuRepository.save(courseMgmt);

            Menu onlineCourses = Menu.builder()
                    .name("Online Courses")
                    .url("/courses/online")
                    .icon("bi-globe")
                    .displayOrder(1)
                    .status(true)
                    .roles("ADMIN,TEACHER")
                    .parent(courseMgmt)
                    .children(new ArrayList<>())
                    .build();
            menuRepository.save(onlineCourses);

            Menu offlineCourses = Menu.builder()
                    .name("Offline Courses")
                    .url("/courses/offline")
                    .icon("bi-building")
                    .displayOrder(2)
                    .status(true)
                    .roles("ADMIN,TEACHER")
                    .parent(courseMgmt)
                    .children(new ArrayList<>())
                    .build();
            menuRepository.save(offlineCourses);

            Menu myCourses = Menu.builder()
                    .name("My Courses")
                    .url("/my-courses")
                    .icon("bi-star")
                    .displayOrder(4)
                    .status(true)
                    .roles("STUDENT")
                    .children(new ArrayList<>())
                    .build();
            menuRepository.save(myCourses);

            Menu systemMgmt = Menu.builder()
                    .name("System Management")
                    .url("")
                    .icon("bi-gear")
                    .displayOrder(5)
                    .status(true)
                    .roles("ADMIN")
                    .children(new ArrayList<>())
                    .build();
            systemMgmt = menuRepository.save(systemMgmt);

            Menu rolesSub = Menu.builder()
                    .name("Roles")
                    .url("/system/roles")
                    .icon("bi-shield-lock")
                    .displayOrder(1)
                    .status(true)
                    .roles("ADMIN")
                    .parent(systemMgmt)
                    .children(new ArrayList<>())
                    .build();
            menuRepository.save(rolesSub);

            Menu usersSub = Menu.builder()
                    .name("Users")
                    .url("/system/users")
                    .icon("bi-person-check")
                    .displayOrder(2)
                    .status(true)
                    .roles("ADMIN")
                    .parent(systemMgmt)
                    .children(new ArrayList<>())
                    .build();
            menuRepository.save(usersSub);

            log.info("Menu data seeded successfully.");
        } else {
            log.info("Database already contains menu data. Skipping seed.");
        }
    }
}
