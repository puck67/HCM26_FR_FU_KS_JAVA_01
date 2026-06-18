package com.lms.service;

import com.lms.main.AppConfig;
import com.lms.model.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseServiceTest {

    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        courseService = context.getBean(CourseService.class);
    }

    @AfterEach
    public void tearDown() {
        File file = new File("courses.dat");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSaveAndGetCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Java", "Author", "Desc", 30));
        
        courseService.saveCourses(courses);
        
        List<Course> retrieved = courseService.getCourses();
        assertNotNull(retrieved);
        assertEquals(1, retrieved.size());
        assertEquals("Java", retrieved.get(0).getTitle());
        assertEquals("Author", retrieved.get(0).getInstructorName());
    }
}
