package com.example.lms.config;

import com.example.lms.entity.Account;
import com.example.lms.entity.Menu;
import com.example.lms.entity.Role;
import com.example.lms.repository.AccountRepository;
import com.example.lms.repository.MenuRepository;
import com.example.lms.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AccountRepository accountRepository, RoleRepository roleRepository, MenuRepository menuRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.menuRepository = menuRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed Roles
        Role adminRole = createRoleIfNotFound("ADMIN");
        Role teacherRole = createRoleIfNotFound("TEACHER");
        Role studentRole = createRoleIfNotFound("STUDENT");

        if (accountRepository.count() == 0) {
            Account admin = new Account();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getRoles().add(adminRole);
            accountRepository.save(admin);

            Account teacher = new Account();
            teacher.setUsername("teacher");
            teacher.setPassword(passwordEncoder.encode("teacher123"));
            teacher.getRoles().add(teacherRole);
            accountRepository.save(teacher);
            
            // Add another teacher for dropdown selection testing
            Account teacher2 = new Account();
            teacher2.setUsername("johndoe");
            teacher2.setPassword(passwordEncoder.encode("teacher123"));
            teacher2.getRoles().add(teacherRole);
            accountRepository.save(teacher2);

            Account student = new Account();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student123"));
            student.getRoles().add(studentRole);
            accountRepository.save(student);

            System.out.println("Default accounts and roles created.");
        }

        if (menuRepository.count() == 0) {
            createMenu("Dashboard", "/", "fa-solid fa-house", "ADMIN", 1);
            createMenu("Quản lý Khóa học", "/courses", "fa-solid fa-book", "ADMIN", 2);
            createMenu("Quản lý Sinh viên", "/students", "fa-solid fa-users", "ADMIN", 3);
            createMenu("Quản lý Menu", "/menus", "fa-solid fa-bars", "ADMIN", 4);

            createMenu("Trang chủ", "/", "fa-solid fa-house", "TEACHER", 1);
            createMenu("Khóa học của tôi", "/courses", "fa-solid fa-book", "TEACHER", 2);

            createMenu("Trang chủ", "/", "fa-solid fa-house", "STUDENT", 1);
            createMenu("Khóa học đang học", "/courses", "fa-solid fa-book", "STUDENT", 2);

            System.out.println("Default menus created.");
        }
    }

    private Role createRoleIfNotFound(String name) {
        Optional<Role> roleOpt = roleRepository.findByName(name);
        if (roleOpt.isPresent()) {
            return roleOpt.get();
        }
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    private void createMenu(String title, String url, String icon, String role, Integer orderIndex) {
        Menu menu = new Menu();
        menu.setTitle(title);
        menu.setUrl(url);
        menu.setIcon(icon);
        menu.setRole(role);
        menu.setOrderIndex(orderIndex);
        menuRepository.save(menu);
    }
}
