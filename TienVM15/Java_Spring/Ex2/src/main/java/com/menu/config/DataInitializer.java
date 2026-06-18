package com.menu.config;

import com.menu.entity.Menu;
import com.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MenuService menuService;

    @Override
    public void run(String... args) throws Exception {
        if (menuService.countAll() > 0) {
            return;
        }

        // 1. Dashboard
        Menu dashboard = Menu.builder()
                .name("Dashboard")
                .url("/")
                .icon("speedometer2")
                .displayOrder(1)
                .status(true)
                .build();
        menuService.save(dashboard);

        // 2. User Management
        Menu userMgmt = Menu.builder()
                .name("User Management")
                .url("#")
                .icon("people")
                .displayOrder(2)
                .status(true)
                .build();
        menuService.save(userMgmt);

        // 2.1 Student Management
        Menu studentMgmt = Menu.builder()
                .name("Student Management")
                .url("/students")
                .icon("person")
                .parent(userMgmt)
                .displayOrder(1)
                .status(true)
                .build();
        menuService.save(studentMgmt);

        // 2.2 Lecturer Management
        Menu lecturerMgmt = Menu.builder()
                .name("Lecturer Management")
                .url("/lecturers")
                .icon("person-workspace")
                .parent(userMgmt)
                .displayOrder(2)
                .status(true)
                .build();
        menuService.save(lecturerMgmt);

        // 3. Training Management
        Menu trainingMgmt = Menu.builder()
                .name("Training Management")
                .url("#")
                .icon("journal-text")
                .displayOrder(3)
                .status(true)
                .build();
        menuService.save(trainingMgmt);

        // 3.1 Subject Management
        Menu subjectMgmt = Menu.builder()
                .name("Subject Management")
                .url("/subjects")
                .icon("journal")
                .parent(trainingMgmt)
                .displayOrder(1)
                .status(true)
                .build();
        menuService.save(subjectMgmt);

        // 3.2 Course Management
        Menu courseMgmt = Menu.builder()
                .name("Course Management")
                .url("/courses")
                .icon("bookmark")
                .parent(trainingMgmt)
                .displayOrder(2)
                .status(true)
                .build();
        menuService.save(courseMgmt);

        // 3.2.1 Online Courses (3rd level bonus)
        Menu onlineCourses = Menu.builder()
                .name("Online Courses")
                .url("/courses/online")
                .icon("globe")
                .parent(courseMgmt)
                .displayOrder(1)
                .status(true)
                .build();
        menuService.save(onlineCourses);

        // 3.2.2 Offline Courses (3rd level bonus)
        Menu offlineCourses = Menu.builder()
                .name("Offline Courses")
                .url("/courses/offline")
                .icon("building")
                .parent(courseMgmt)
                .displayOrder(2)
                .status(true)
                .build();
        menuService.save(offlineCourses);
    }
}
