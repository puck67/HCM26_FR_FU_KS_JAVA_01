package com.fpt.lms.service;

import com.fpt.lms.model.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {

    private CourseService courseService;
    private static final String FILE_PATH = "courses.dat";

    @BeforeEach
    void setUp() {
        courseService = new CourseService();
        cleanFile();
    }

    @AfterEach
    void tearDown() {
        cleanFile();
    }

    private void cleanFile() {
        new File(FILE_PATH).delete();
    }

    @Test
    void testSaveAndGetCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Java basics", "John", "Learn Java", 12));
        courses.add(new Course("Spring context", "Mary", "Learn Spring", 20));

        courseService.saveCourses(courses);

        List<Course> loadedCourses = courseService.getCourses();
        assertEquals(2, loadedCourses.size());
        assertEquals("Java basics", loadedCourses.get(0).getTitle());
        assertEquals(20, loadedCourses.get(1).getDurationHours());
    }

    @Test
    void testGetCoursesWhenFileDoesNotExist() {
        List<Course> loadedCourses = courseService.getCourses();
        assertNotNull(loadedCourses);
        assertTrue(loadedCourses.isEmpty());
    }
}
