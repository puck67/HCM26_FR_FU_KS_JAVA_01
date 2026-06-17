package com.lms.service;

import com.lms.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LmsServiceTest {

    @Autowired
    private LmsService lmsService;

    @Test
    public void testMarkdownRendering() {
        String md = "# Hello World\n\nThis is a **test**.";
        String html = lmsService.renderMarkdown(md);
        assertTrue(html.contains("<h1>Hello World</h1>"));
        assertTrue(html.contains("<strong>test</strong>"));
    }

    @Test
    public void testSaveCourseAndRecalculateCategories() {
        Course course = new Course("Test Course", "Test Description", "Test Content", 2, "TestCat1, TestCat2");
        Course saved = lmsService.saveCourse(course);
        assertNotNull(saved.getId());

        var categories = lmsService.getCategoryCloud();
        assertFalse(categories.isEmpty());
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("TestCat1")));
    }
}
