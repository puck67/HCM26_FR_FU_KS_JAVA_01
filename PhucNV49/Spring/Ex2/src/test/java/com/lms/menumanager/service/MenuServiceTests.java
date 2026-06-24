package com.lms.menumanager.service;

import com.lms.menumanager.entity.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MenuServiceTests {

    @Autowired
    private MenuService menuService;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    @Test
    public void testDatabaseSeeded() {
        long count = menuService.getTotalMenus();
        assertTrue(count > 0, "Database should be seeded on startup");
        
        long parents = menuService.getTotalParentMenus();
        long subs = menuService.getTotalSubMenus();
        assertEquals(count, parents + subs, "Total menus should equal parent + sub menus");
    }

    @Test
    public void testRoleBasedFiltering() {
        // ADMIN should see User Management
        List<Menu> adminTree = menuService.getMenuTreeForRole("ADMIN");
        boolean hasUserMgmt = adminTree.stream().anyMatch(m -> m.getName().equals("User Management"));
        assertTrue(hasUserMgmt, "ADMIN role should see User Management");

        // STUDENT should NOT see User Management, but should see My Courses
        List<Menu> studentTree = menuService.getMenuTreeForRole("STUDENT");
        boolean studentHasUserMgmt = studentTree.stream().anyMatch(m -> m.getName().equals("User Management"));
        assertFalse(studentHasUserMgmt, "STUDENT role should not see User Management");

        boolean studentHasMyCourses = studentTree.stream().anyMatch(m -> m.getName().equals("My Courses"));
        assertTrue(studentHasMyCourses, "STUDENT role should see My Courses");
        
        // TEACHER should see Training Management but not User Management
        List<Menu> teacherTree = menuService.getMenuTreeForRole("TEACHER");
        boolean teacherHasUserMgmt = teacherTree.stream().anyMatch(m -> m.getName().equals("User Management"));
        assertFalse(teacherHasUserMgmt, "TEACHER role should not see User Management");
        
        boolean teacherHasTraining = teacherTree.stream().anyMatch(m -> m.getName().equals("Training Management"));
        assertTrue(teacherHasTraining, "TEACHER role should see Training Management");
    }

    @Test
    public void testCascadeDeletionPrevention() {
        // User Management is seeded and has submenus
        List<Menu> rootMenus = menuService.getRootMenus();
        Menu userMgmt = rootMenus.stream()
                .filter(m -> m.getName().equals("User Management"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(userMgmt, "User Management root menu should exist");
        assertFalse(userMgmt.getChildren().isEmpty(), "User Management should have submenus");

        // Try deleting User Management without cascade, should throw IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            menuService.deleteMenu(userMgmt.getId(), false);
        }, "Should throw IllegalStateException when trying to delete menu with submenus without cascade");
    }

    @Test
    public void testCascadeDeletionSuccess() {
        // Create a parent and a child
        Menu parent = new Menu();
        parent.setName("Test Parent");
        parent.setDisplayOrder(99);
        parent.setStatus(true);
        parent.setRoles("ADMIN");
        parent = menuService.saveMenu(parent);

        Menu child = new Menu();
        child.setName("Test Child");
        child.setDisplayOrder(1);
        child.setStatus(true);
        child.setRoles("ADMIN");
        parent.addChild(child);
        child = menuService.saveMenu(child);

        Long parentId = parent.getId();
        Long childId = child.getId();

        // Perform cascade delete on parent
        menuService.deleteMenu(parentId, true);

        entityManager.flush();
        entityManager.clear();

        // Verify both parent and child are deleted
        assertNull(menuService.getMenuById(parentId), "Parent should be deleted");
        assertNull(menuService.getMenuById(childId), "Child should be deleted recursively");
    }
}
