package com.example.demo.config;

import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();

        if (menuRepository.count() > 0) {
            return;
        }

        Role admin = roleRepository.findByName("ADMIN").orElseThrow();
        Role teacher = roleRepository.findByName("TEACHER").orElseThrow();
        Role student = roleRepository.findByName("STUDENT").orElseThrow();

        menu("Dashboard", "/", "bi-speedometer2", null, 1, Set.of(admin, teacher, student));
        Menu userManagement = menu("User Management", null, "bi-people", null, 2, Set.of(admin));
        menu("Student Management", "/students", "bi-person-vcard", userManagement, 1, Set.of(admin));
        menu("Lecturer Management", "/lecturers", "bi-person-workspace", userManagement, 2, Set.of(admin));

        Menu trainingManagement = menu("Training Management", null, "bi-journal-bookmark", null, 3, Set.of(admin, teacher));
        menu("Subject Management", "/subjects", "bi-book", trainingManagement, 1, Set.of(admin));
        Menu courseManagement = menu("Course Management", "/courses", "bi-collection-play", trainingManagement, 2, Set.of(admin, teacher));
        menu("Online Courses", "/courses/online", "bi-wifi", courseManagement, 1, Set.of(admin, teacher));
        menu("Offline Courses", "/courses/offline", "bi-building", courseManagement, 2, Set.of(admin, teacher));

        Menu systemManagement = menu("System Management", null, "bi-gear", null, 4, Set.of(admin));
        menu("Roles", "/system/roles", "bi-shield-lock", systemManagement, 1, Set.of(admin));
        menu("Users", "/system/users", "bi-person-gear", systemManagement, 2, Set.of(admin));

        menu("My Courses", "/my-courses", "bi-mortarboard", null, 5, Set.of(student));
    }

    private void seedRoles() {
        List.of("ADMIN", "TEACHER", "STUDENT").forEach(name ->
                roleRepository.findByName(name).orElseGet(() -> roleRepository.save(new Role(null, name))));
    }

    private Menu menu(String name, String url, String icon, Menu parent, int displayOrder, Set<Role> roles) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setUrl(url);
        menu.setIcon(icon);
        menu.setParent(parent);
        menu.setDisplayOrder(displayOrder);
        menu.setStatus(true);
        menu.setRoles(new HashSet<>(roles));
        return menuRepository.save(menu);
    }
}
