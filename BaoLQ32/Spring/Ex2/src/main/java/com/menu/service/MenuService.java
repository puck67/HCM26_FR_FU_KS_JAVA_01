package com.menu.service;

import com.menu.model.Menu;
import com.menu.repository.MenuRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public List<Menu> getHierarchicalMenus() {
        List<Menu> result = new java.util.ArrayList<>();
        List<Menu> roots = getRootMenus();
        for (Menu root : roots) {
            flatten(root, result);
        }
        return result;
    }

    private void flatten(Menu menu, List<Menu> result) {
        result.add(menu);
        List<Menu> sortedChildren = menu.getChildren().stream()
                .sorted(java.util.Comparator.comparing(Menu::getDisplayOrder))
                .collect(Collectors.toList());
        for (Menu child : sortedChildren) {
            flatten(child, result);
        }
    }

    public List<Menu> getRootMenus() {
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
    }

    public List<Menu> getActiveRootMenus() {
        return menuRepository.findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();
    }

    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    public Menu saveMenu(Menu menu) {
        // Prevent circular reference: a menu cannot be its own parent
        if (menu.getId() != null && menu.getParent() != null) {
            if (menu.getId().equals(menu.getParent().getId())) {
                throw new IllegalArgumentException("A menu cannot be its own parent!");
            }
            // Check if parent is a descendant of the menu to prevent circular trees
            Menu currentParent = menu.getParent();
            while (currentParent != null) {
                if (currentParent.getId().equals(menu.getId())) {
                    throw new IllegalArgumentException("Circular hierarchy detected! A parent cannot be a child of this menu.");
                }
                currentParent = currentParent.getParent();
            }
        }
        return menuRepository.save(menu);
    }

    public void deleteMenu(Long id, boolean cascade) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found with id: " + id));

        if (!menu.getChildren().isEmpty()) {
            if (!cascade) {
                throw new IllegalStateException("Cannot delete menu because it has submenus. Please delete submenus first or select Cascade Delete.");
            }
            // Cascade Delete: Delete children recursively
            // Note: Since we are in @Transactional, we can delete them.
            // We manually delete child nodes to avoid database constraint errors.
            deleteMenuAndChildren(menu);
        } else {
            menuRepository.delete(menu);
        }
    }

    private void deleteMenuAndChildren(Menu menu) {
        // Copy the list to avoid ConcurrentModificationException during recursion
        List<Menu> childrenCopy = List.copyOf(menu.getChildren());
        for (Menu child : childrenCopy) {
            deleteMenuAndChildren(child);
        }
        menuRepository.delete(menu);
    }

    // Filter active menus by role, recursively filtering their children
    public List<Menu> getActiveRootMenusForRole(String role) {
        List<Menu> rootMenus = getActiveRootMenus();
        return filterMenusByRole(rootMenus, role);
    }

    private List<Menu> filterMenusByRole(List<Menu> menus, String role) {
        return menus.stream()
                .filter(menu -> menu.hasRole(role))
                .map(menu -> {
                    // Clone or use the object, but since it's lazy/cached, we can create a filtered representation
                    // or just filter children list.
                    // For UI rendering, we can filter children matching the role.
                    Menu filteredMenu = new Menu();
                    filteredMenu.setId(menu.getId());
                    filteredMenu.setName(menu.getName());
                    filteredMenu.setUrl(menu.getUrl());
                    filteredMenu.setIcon(menu.getIcon());
                    filteredMenu.setParent(menu.getParent());
                    filteredMenu.setDisplayOrder(menu.getDisplayOrder());
                    filteredMenu.setStatus(menu.getStatus());
                    filteredMenu.setRoles(menu.getRoles());
                    
                    List<Menu> filteredChildren = filterMenusByRole(menu.getChildren(), role);
                    filteredMenu.setChildren(filteredChildren);
                    return filteredMenu;
                })
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void seedInitialData() {
        if (menuRepository.count() > 0) {
            return; // Data already exists
        }

        // 1. Dashboard
        Menu dashboard = new Menu();
        dashboard.setName("Dashboard");
        dashboard.setUrl("/dashboard");
        dashboard.setIcon("bi bi-speedometer2");
        dashboard.setDisplayOrder(1);
        dashboard.setStatus(true);
        dashboard.setRoles("ADMIN,TEACHER,STUDENT");
        dashboard = menuRepository.save(dashboard);

        // 2. User Management
        Menu userMgmt = new Menu();
        userMgmt.setName("User Management");
        userMgmt.setUrl(null);
        userMgmt.setIcon("bi bi-people");
        userMgmt.setDisplayOrder(2);
        userMgmt.setStatus(true);
        userMgmt.setRoles("ADMIN");
        userMgmt = menuRepository.save(userMgmt);

        // 3. Student Management (Child of User Management)
        Menu studentMgmt = new Menu();
        studentMgmt.setName("Student Management");
        studentMgmt.setUrl("/students");
        studentMgmt.setIcon("bi bi-mortarboard");
        studentMgmt.setParent(userMgmt);
        studentMgmt.setDisplayOrder(1);
        studentMgmt.setStatus(true);
        studentMgmt.setRoles("ADMIN");
        menuRepository.save(studentMgmt);

        // 4. Lecturer Management (Child of User Management)
        Menu lecturerMgmt = new Menu();
        lecturerMgmt.setName("Lecturer Management");
        lecturerMgmt.setUrl("/lecturers");
        lecturerMgmt.setIcon("bi bi-person-badge");
        lecturerMgmt.setParent(userMgmt);
        lecturerMgmt.setDisplayOrder(2);
        lecturerMgmt.setStatus(true);
        lecturerMgmt.setRoles("ADMIN");
        menuRepository.save(lecturerMgmt);

        // 5. Training Management
        Menu trainingMgmt = new Menu();
        trainingMgmt.setName("Training Management");
        trainingMgmt.setUrl(null);
        trainingMgmt.setIcon("bi bi-journal-bookmark");
        trainingMgmt.setDisplayOrder(3);
        trainingMgmt.setStatus(true);
        trainingMgmt.setRoles("ADMIN,TEACHER");
        trainingMgmt = menuRepository.save(trainingMgmt);

        // 6. Subject Management (Child of Training Management)
        Menu subjectMgmt = new Menu();
        subjectMgmt.setName("Subject Management");
        subjectMgmt.setUrl("/subjects");
        subjectMgmt.setIcon("bi bi-book");
        subjectMgmt.setParent(trainingMgmt);
        subjectMgmt.setDisplayOrder(1);
        subjectMgmt.setStatus(true);
        subjectMgmt.setRoles("ADMIN,TEACHER");
        menuRepository.save(subjectMgmt);

        // 7. Course Management (Child of Training Management)
        Menu courseMgmt = new Menu();
        courseMgmt.setName("Course Management");
        courseMgmt.setUrl("/courses");
        courseMgmt.setIcon("bi bi-list-task");
        courseMgmt.setParent(trainingMgmt);
        courseMgmt.setDisplayOrder(2);
        courseMgmt.setStatus(true);
        courseMgmt.setRoles("ADMIN,TEACHER");
        courseMgmt = menuRepository.save(courseMgmt);

        // Bonus: 3rd level menu support!
        // Online Courses (Child of Course Management)
        Menu onlineCourses = new Menu();
        onlineCourses.setName("Online Courses");
        onlineCourses.setUrl("/courses/online");
        onlineCourses.setIcon("bi bi-laptop");
        onlineCourses.setParent(courseMgmt);
        onlineCourses.setDisplayOrder(1);
        onlineCourses.setStatus(true);
        onlineCourses.setRoles("ADMIN,TEACHER");
        menuRepository.save(onlineCourses);

        // Offline Courses (Child of Course Management)
        Menu offlineCourses = new Menu();
        offlineCourses.setName("Offline Courses");
        offlineCourses.setUrl("/courses/offline");
        offlineCourses.setIcon("bi bi-building");
        offlineCourses.setParent(courseMgmt);
        offlineCourses.setDisplayOrder(2);
        offlineCourses.setStatus(true);
        offlineCourses.setRoles("ADMIN,TEACHER");
        menuRepository.save(offlineCourses);

        // 8. Exam Management (Future function)
        Menu examMgmt = new Menu();
        examMgmt.setName("Exam Management");
        examMgmt.setUrl(null);
        examMgmt.setIcon("bi bi-file-earmark-text");
        examMgmt.setDisplayOrder(4);
        examMgmt.setStatus(true);
        examMgmt.setRoles("ADMIN,TEACHER");
        examMgmt = menuRepository.save(examMgmt);

        // 9. Question Bank (Child of Exam Management)
        Menu questionBank = new Menu();
        questionBank.setName("Question Bank");
        questionBank.setUrl("/questions");
        questionBank.setIcon("bi bi-question-circle");
        questionBank.setParent(examMgmt);
        questionBank.setDisplayOrder(1);
        questionBank.setStatus(true);
        questionBank.setRoles("ADMIN,TEACHER");
        menuRepository.save(questionBank);

        // 10. Exam (Child of Exam Management)
        Menu exam = new Menu();
        exam.setName("Exam");
        exam.setUrl("/exams");
        exam.setIcon("bi bi-pencil-square");
        exam.setParent(examMgmt);
        exam.setDisplayOrder(2);
        exam.setStatus(true);
        exam.setRoles("ADMIN,TEACHER");
        menuRepository.save(exam);

        // 11. Result (Child of Exam Management)
        Menu result = new Menu();
        result.setName("Result");
        result.setUrl("/results");
        result.setIcon("bi bi-award");
        result.setParent(examMgmt);
        result.setDisplayOrder(3);
        result.setStatus(true);
        result.setRoles("ADMIN,TEACHER,STUDENT");
        menuRepository.save(result);

        // 12. System Management (Future function)
        Menu systemMgmt = new Menu();
        systemMgmt.setName("System Management");
        systemMgmt.setUrl(null);
        systemMgmt.setIcon("bi bi-gear");
        systemMgmt.setDisplayOrder(5);
        systemMgmt.setStatus(true);
        systemMgmt.setRoles("ADMIN");
        systemMgmt = menuRepository.save(systemMgmt);

        // 13. Roles (Child of System Management)
        Menu rolesMenu = new Menu();
        rolesMenu.setName("Roles");
        rolesMenu.setUrl("/roles");
        rolesMenu.setIcon("bi bi-shield-lock");
        rolesMenu.setParent(systemMgmt);
        rolesMenu.setDisplayOrder(1);
        rolesMenu.setStatus(true);
        rolesMenu.setRoles("ADMIN");
        menuRepository.save(rolesMenu);

        // 14. Users (Child of System Management)
        Menu usersMenu = new Menu();
        usersMenu.setName("Users");
        usersMenu.setUrl("/users");
        usersMenu.setIcon("bi bi-person-gear");
        usersMenu.setParent(systemMgmt);
        usersMenu.setDisplayOrder(2);
        usersMenu.setStatus(true);
        usersMenu.setRoles("ADMIN");
        menuRepository.save(usersMenu);

        // 15. Student-specific Menu
        Menu myCourses = new Menu();
        myCourses.setName("My Courses");
        myCourses.setUrl("/my-courses");
        myCourses.setIcon("bi bi-card-checklist");
        myCourses.setDisplayOrder(6);
        myCourses.setStatus(true);
        myCourses.setRoles("STUDENT");
        menuRepository.save(myCourses);
    }
}
