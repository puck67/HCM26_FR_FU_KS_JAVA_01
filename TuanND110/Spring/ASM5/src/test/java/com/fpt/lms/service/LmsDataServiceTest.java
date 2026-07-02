package com.fpt.lms.service;

import com.fpt.lms.model.LmsUser;
import com.fpt.lms.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LmsDataServiceTest {

    private LmsDataService lmsDataService;

    @BeforeEach
    void setUp() {
        lmsDataService = new LmsDataService();
        cleanFiles();
    }

    @AfterEach
    void tearDown() {
        cleanFiles();
    }

    private void cleanFiles() {
        new File("users.dat").delete();
        new File("students.dat").delete();
    }

    @Test
    void testSaveAndGetUsers() {
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(1L, "Alice", "alice@lms.com"));
        users.add(new LmsUser(2L, "Bob", "bob@lms.com"));

        lmsDataService.saveUsers(users);

        List<LmsUser> loadedUsers = lmsDataService.getUsers();
        assertEquals(2, loadedUsers.size());
        assertEquals("Alice", loadedUsers.get(0).getName());
        assertEquals("bob@lms.com", loadedUsers.get(1).getEmail());
    }

    @Test
    void testGetUsersWhenFileDoesNotExist() {
        List<LmsUser> loadedUsers = lmsDataService.getUsers();
        assertNotNull(loadedUsers);
        assertTrue(loadedUsers.isEmpty());
    }

    @Test
    void testSaveAndGetStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "Charlie", 3.8));
        students.add(new Student(2L, "David", 3.5));

        lmsDataService.saveStudents(students);

        List<Student> loadedStudents = lmsDataService.getStudents();
        assertEquals(2, loadedStudents.size());
        assertEquals("Charlie", loadedStudents.get(0).getName());
        assertEquals(3.5, loadedStudents.get(1).getGpa());
    }

    @Test
    void testGetStudentsWhenFileDoesNotExist() {
        List<Student> loadedStudents = lmsDataService.getStudents();
        assertNotNull(loadedStudents);
        assertTrue(loadedStudents.isEmpty());
    }
}
