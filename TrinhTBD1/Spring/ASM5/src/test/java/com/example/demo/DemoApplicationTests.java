package com.example.demo;

import com.example.demo.model.Instructor;
import com.example.demo.model.LmsUser;
import com.example.demo.model.Student;
import com.example.demo.service.LmsDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private LmsDataService dataService;

    @AfterEach
    void cleanUp() {
        new File("users.dat").delete();
        new File("students.dat").delete();
    }

    @Test
    void contextLoads() {
        assertNotNull(dataService, "LmsDataService bean should be loaded successfully");
    }

    @Test
    void testSaveAndGetUsers_HappyPath() {
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(1, "John Doe", "john.doe@fpt.com"));
        users.add(new Instructor(2, "Jane Doe", "jane.doe@fpt.com", "Engineering", "Spring Framework Expert"));

        dataService.saveUsers(users);

        List<LmsUser> retrieved = dataService.getUsers();
        assertNotNull(retrieved, "Retrieved users list should not be null");
        assertEquals(2, retrieved.size(), "Retrieved list size should match the saved list size");
        assertEquals("John Doe", retrieved.get(0).getName());
        assertEquals("Engineering", ((Instructor) retrieved.get(1)).getDepartment());
    }

    @Test
    void testSaveAndGetStudents_HappyPath() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(101, "Jane Smith", 3.75));

        dataService.saveStudents(students);

        List<Student> retrieved = dataService.getStudents();
        assertNotNull(retrieved, "Retrieved students list should not be null");
        assertEquals(1, retrieved.size(), "Retrieved list size should match the saved list size");
        assertEquals("Jane Smith", retrieved.get(0).getName());
        assertEquals(3.75, retrieved.get(0).getGpa());
    }

    @Test
    void testGetUsers_WhenFileDoesNotExist() {
        new File("users.dat").delete();
        List<LmsUser> retrieved = dataService.getUsers();
        assertNotNull(retrieved, "Should return empty list instead of null when file doesn't exist");
        assertTrue(retrieved.isEmpty(), "List should be empty");
    }

    @Test
    void testValidation_InvalidEmail() {
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(1, "John Doe", "invalid-email"));
        assertThrows(IllegalArgumentException.class, () -> {
            dataService.saveUsers(users);
        }, "Should throw IllegalArgumentException for invalid email");
    }

    @Test
    void testValidation_InvalidGpa() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(101, "Jane Smith", 5.0));
        assertThrows(IllegalArgumentException.class, () -> {
            dataService.saveStudents(students);
        }, "Should throw IllegalArgumentException for GPA > 4.0");
    }

    @Test
    void testValidation_NegativeId() {
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(-1, "John Doe", "john.doe@fpt.com"));
        assertThrows(IllegalArgumentException.class, () -> {
            dataService.saveUsers(users);
        }, "Should throw IllegalArgumentException for negative userId");
    }
}
