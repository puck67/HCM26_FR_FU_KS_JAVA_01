package com.example.demo;

import com.example.demo.model.Menu;
import com.example.demo.repository.MenuRepository;
import com.example.demo.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DemoApplicationTests {

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuRepository menuRepository;

	@Test
	void contextLoads() {
		assertNotNull(menuService, "MenuService should be successfully injected");
	}

	@Test
	void testMenuCrudOperations() {
		Menu testMenu = Menu.builder()
				.name("Test Menu item")
				.url("/test-item")
				.displayOrder(10)
				.status(true)
				.roles("ADMIN")
				.build();
		Menu saved = menuService.save(testMenu);
		assertNotNull(saved.getId(), "Saved menu should have an assigned ID");

		Optional<Menu> found = menuService.findById(saved.getId());
		assertTrue(found.isPresent(), "Menu should be findable by ID");
		assertEquals("Test Menu item", found.get().getName());

		found.get().setName("Updated Test Menu item");
		Menu updated = menuService.save(found.get());
		assertEquals("Updated Test Menu item", updated.getName());

		menuService.deleteById(updated.getId());
		assertFalse(menuService.findById(updated.getId()).isPresent(), "Deleted menu should not exist");
	}

	@Test
	void testCyclePreventionSelfReferencing() {
		Menu menu = Menu.builder()
				.name("Self Parent Menu")
				.displayOrder(1)
				.status(true)
				.build();
		Menu saved = menuService.save(menu);

		saved.setParent(saved);
		
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			menuService.validateMenu(saved);
		}, "Setting a menu as its own parent should throw IllegalArgumentException");

		assertTrue(exception.getMessage().contains("own parent"));
	}

	@Test
	void testCyclePreventionDescendant() {
		Menu root = Menu.builder()
				.name("Root Parent")
				.displayOrder(1)
				.status(true)
				.build();
		root = menuService.save(root);

		Menu child = Menu.builder()
				.name("Child Menu")
				.displayOrder(1)
				.status(true)
				.parent(root)
				.build();
		child = menuService.save(child);

		root.setParent(child);
		Menu finalRoot = root;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			menuService.validateMenu(finalRoot);
		}, "Setting a parent to its descendant should throw IllegalArgumentException to prevent cycle");

		assertTrue(exception.getMessage().contains("cycle dependency"));
	}

	@Test
	void testDuplicateNameValidationUnderSameParent() {
		Menu first = Menu.builder()
				.name("Duplicate Test")
				.displayOrder(1)
				.status(true)
				.build();
		menuService.save(first);

		Menu second = Menu.builder()
				.name("Duplicate Test")
				.displayOrder(2)
				.status(true)
				.build();

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			menuService.validateMenu(second);
		}, "Duplicate menu name under the same parent should throw IllegalArgumentException");

		assertTrue(exception.getMessage().contains("already exists"));
	}

	@Test
	void testSidebarFilterByRole() {
		List<Menu> adminMenus = menuService.getSidebarMenus("ADMIN");
		assertNotNull(adminMenus);
		
		List<Menu> studentMenus = menuService.getSidebarMenus("STUDENT");
		assertNotNull(studentMenus);

		boolean hasMyCoursesStudent = studentMenus.stream().anyMatch(m -> "My Courses".equals(m.getName()));
		boolean hasMyCoursesAdmin = adminMenus.stream().anyMatch(m -> "My Courses".equals(m.getName()));

		assertTrue(hasMyCoursesStudent, "Student sidebar should contain 'My Courses'");
		assertFalse(hasMyCoursesAdmin, "Admin sidebar should not contain 'My Courses'");
	}
}
