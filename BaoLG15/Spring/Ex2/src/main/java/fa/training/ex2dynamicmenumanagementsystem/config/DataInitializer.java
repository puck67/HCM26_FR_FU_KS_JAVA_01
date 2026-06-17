package fa.training.ex2dynamicmenumanagementsystem.config;

import fa.training.ex2dynamicmenumanagementsystem.entities.Menu;
import fa.training.ex2dynamicmenumanagementsystem.repositories.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MenuRepository menuRepository;

    @Override
    public void run(String... args) throws Exception {
        if (menuRepository.count() == 0) {
            // 1. Dashboard
            Menu dashboard = Menu.builder()
                    .name("Dashboard")
                    .url("/dashboard")
                    .icon("bi bi-speedometer2")
                    .displayOrder(1)
                    .status(true)
                    .roles("ADMIN,TEACHER,STUDENT")
                    .build();
            menuRepository.save(dashboard);

            // 2. User Management
            Menu userMgmt = Menu.builder()
                    .name("User Management")
                    .url(null)
                    .icon("bi bi-people")
                    .displayOrder(2)
                    .status(true)
                    .roles("ADMIN")
                    .build();
            userMgmt = menuRepository.save(userMgmt);

            // 3. Student Management (child of User Mgmt)
            Menu studentMgmt = Menu.builder()
                    .name("Student Management")
                    .url("/students")
                    .icon("bi bi-person-fill")
                    .parent(userMgmt)
                    .displayOrder(1)
                    .status(true)
                    .roles("ADMIN")
                    .build();
            menuRepository.save(studentMgmt);

            // 4. Lecturer Management (child of User Mgmt)
            Menu lecturerMgmt = Menu.builder()
                    .name("Lecturer Management")
                    .url("/lecturers")
                    .icon("bi bi-person-workspace")
                    .parent(userMgmt)
                    .displayOrder(2)
                    .status(true)
                    .roles("ADMIN")
                    .build();
            menuRepository.save(lecturerMgmt);

            // 5. Training Management
            Menu trainingMgmt = Menu.builder()
                    .name("Training Management")
                    .url(null)
                    .icon("bi bi-journal-bookmark")
                    .displayOrder(3)
                    .status(true)
                    .roles("ADMIN,TEACHER")
                    .build();
            trainingMgmt = menuRepository.save(trainingMgmt);

            // 6. Subject Management (child of Training Mgmt)
            Menu subjectMgmt = Menu.builder()
                    .name("Subject Management")
                    .url("/subjects")
                    .icon("bi bi-book")
                    .parent(trainingMgmt)
                    .displayOrder(1)
                    .status(true)
                    .roles("ADMIN,TEACHER")
                    .build();
            menuRepository.save(subjectMgmt);

            // 7. Course Management (child of Training Mgmt)
            Menu courseMgmt = Menu.builder()
                    .name("Course Management")
                    .url("/courses")
                    .icon("bi bi-laptop")
                    .parent(trainingMgmt)
                    .displayOrder(2)
                    .status(true)
                    .roles("ADMIN,TEACHER")
                    .build();
            courseMgmt = menuRepository.save(courseMgmt);

            // 8. Online Courses (child of Course Mgmt - LEVEL 3!)
            Menu onlineCourses = Menu.builder()
                    .name("Online Courses")
                    .url("/courses/online")
                    .icon("bi bi-globe")
                    .parent(courseMgmt)
                    .displayOrder(1)
                    .status(true)
                    .roles("ADMIN,TEACHER")
                    .build();
            menuRepository.save(onlineCourses);

            // 9. Offline Courses (child of Course Mgmt - LEVEL 3!)
            Menu offlineCourses = Menu.builder()
                    .name("Offline Courses")
                    .url("/courses/offline")
                    .icon("bi bi-building")
                    .parent(courseMgmt)
                    .displayOrder(2)
                    .status(true)
                    .roles("ADMIN,TEACHER")
                    .build();
            menuRepository.save(offlineCourses);

            // 10. System Management
            Menu systemMgmt = Menu.builder()
                    .name("System Management")
                    .url(null)
                    .icon("bi bi-gear")
                    .displayOrder(4)
                    .status(true)
                    .roles("ADMIN")
                    .build();
            systemMgmt = menuRepository.save(systemMgmt);

            // 11. Roles (child of System Mgmt)
            Menu rolesMenu = Menu.builder()
                    .name("Roles")
                    .url("/roles")
                    .icon("bi bi-shield-lock")
                    .parent(systemMgmt)
                    .displayOrder(1)
                    .status(true)
                    .roles("ADMIN")
                    .build();
            menuRepository.save(rolesMenu);

            // 12. Users (child of System Mgmt)
            Menu usersMenu = Menu.builder()
                    .name("Users")
                    .url("/users")
                    .icon("bi bi-person-lines")
                    .parent(systemMgmt)
                    .displayOrder(2)
                    .status(true)
                    .roles("ADMIN")
                    .build();
            menuRepository.save(usersMenu);

            // 13. My Courses (visible to Student)
            Menu myCourses = Menu.builder()
                    .name("My Courses")
                    .url("/my-courses")
                    .icon("bi bi-mortarboard")
                    .displayOrder(5)
                    .status(true)
                    .roles("STUDENT")
                    .build();
            menuRepository.save(myCourses);
        }
    }
}
