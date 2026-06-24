package com.example.ex2.config;

import com.example.ex2.entity.Menu;
import com.example.ex2.repository.MenuRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MenuDataInitializer implements CommandLineRunner {

    private final MenuRepository menuRepository;

    public MenuDataInitializer(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (menuRepository.count() == 0) {
            // Seed 1. Dashboard (dành cho cả 3 roles)
            Menu dashboard = new Menu("Dashboard", "/", "ADMIN,TEACHER,STUDENT", 1, null);
            menuRepository.save(dashboard);

            // Seed 2. Admin Menus
            Menu userMgmt = new Menu("User Management", "/students", "ADMIN", 2, null);
            menuRepository.save(userMgmt);

            Menu sysMgmt = new Menu("System Management", "/courses", "ADMIN", 3, null);
            menuRepository.save(sysMgmt);

            // Seed 3. Teacher Menus
            Menu courseMgmt = new Menu("Course Management", "/courses", "TEACHER", 2, null);
            menuRepository.save(courseMgmt);

            // Seed 4. Student Menus
            Menu myCourses = new Menu("My Courses", "/enrollments", "STUDENT", 2, null);
            menuRepository.save(myCourses);

            // Seed 5. 3rd Level Menu Bonus Example (Dành cho ADMIN và TEACHER)
            Menu trainingMgmt = new Menu("Training Management", "#", "ADMIN,TEACHER", 4, null);
            Menu savedTrainingMgmt = menuRepository.save(trainingMgmt);

            Menu courseMgmtSub = new Menu("Course Management", "#", "ADMIN,TEACHER", 1, savedTrainingMgmt);
            Menu savedCourseMgmtSub = menuRepository.save(courseMgmtSub);

            Menu onlineCourses = new Menu("Online Courses", "/courses", "ADMIN,TEACHER", 1, savedCourseMgmtSub);
            menuRepository.save(onlineCourses);

            Menu offlineCourses = new Menu("Offline Courses", "/courses", "ADMIN,TEACHER", 2, savedCourseMgmtSub);
            menuRepository.save(offlineCourses);

            System.out.println(">>> Đã khởi tạo dữ liệu mẫu cho hệ thống dynamic Menu (3 levels & Role-based) thành công!");
        }
    }
}
