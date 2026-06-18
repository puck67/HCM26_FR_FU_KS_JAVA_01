package com.lms.service;

import com.lms.main.AppConfig;
import com.lms.model.Instructor;
import com.lms.model.LmsUser;
import com.lms.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LmsDataServiceTest {

    private LmsDataService dataService;

    @BeforeEach
    public void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        dataService = context.getBean(LmsDataService.class);
    }

    @AfterEach
    public void tearDown() {
        File usersFile = new File("users.dat");
        if (usersFile.exists()) {
            usersFile.delete();
        }
        File studentsFile = new File("students.dat");
        if (studentsFile.exists()) {
            studentsFile.delete();
        }
    }

    @Test
    public void testSaveAndGetUsers() {
        List<LmsUser> users = new ArrayList<>();
        users.add(new LmsUser(1, "User1", "u1@fpt.com"));
        users.add(new Instructor(2, "Inst1", "i1@fpt.com", "CS", "Expert"));

        dataService.saveUsers(users);

        List<LmsUser> retrieved = dataService.getUsers();
        assertNotNull(retrieved);
        assertEquals(2, retrieved.size());
        assertEquals("User1", retrieved.get(0).getName());
        assertTrue(retrieved.get(1) instanceof Instructor);
        assertEquals("CS", ((Instructor) retrieved.get(1)).getDepartment());
    }

    @Test
    public void testSaveAndGetStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(101, "Student1", 3.80));

        dataService.saveStudents(students);

        List<Student> retrieved = dataService.getStudents();
        assertNotNull(retrieved);
        assertEquals(1, retrieved.size());
        assertEquals("Student1", retrieved.get(0).getName());
        assertEquals(3.80, retrieved.get(0).getGpa());
    }
}
