package com.fpt.lms.controller;

import com.fpt.lms.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fpt.lms.service.CourseService;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseControllerTest {

    private MockMvc mockMvc;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = Mockito.mock(CourseService.class);
        CourseController courseController = new CourseController(courseService);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    @Test
    void testShowCreateCourseForm() throws Exception {
        mockMvc.perform(get("/courses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_course"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    void testCreateCourseSuccess() throws Exception {
        mockMvc.perform(post("/courses/create")
                        .param("title", "Spring Security Masterclass")
                        .param("instructorName", "John Doe")
                        .param("instructorEmail", "john.doe@example.com")
                        .param("description", "Secure your APIs and webapps completely")
                        .param("durationHours", "36"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));
    }

    @Test
    void testCreateCourseValidationFailure() throws Exception {
        mockMvc.perform(post("/courses/create")
                        .param("title", "Short") // Less than 5 characters or invalid inputs
                        .param("instructorName", "")
                        .param("instructorEmail", "invalid-email")
                        .param("description", "Too short")
                        .param("durationHours", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_course"));
    }

    @Test
    void testShowCreateSuccessPage() throws Exception {
        mockMvc.perform(get("/courses/success"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_success"));
    }

    @Test
    void testListCourses() throws Exception {
        Mockito.when(courseService.findAll()).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("course_list"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    void testDownloadTemplate() throws Exception {
        mockMvc.perform(get("/courses/template"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"course_template.csv\""))
                .andExpect(content().contentType("text/csv; charset=utf-8"));
    }

    @Test
    void testExportCourses() throws Exception {
        Course course = Course.builder()
                .title("Spring Testing")
                .instructorName("Jane Doe")
                .instructorEmail("jane@example.com")
                .description("Detailed testing guide for Spring Boot apps")
                .durationHours(15)
                .build();
        Mockito.when(courseService.findAll()).thenReturn(java.util.Collections.singletonList(course));

        mockMvc.perform(get("/courses/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"courses_export.csv\""))
                .andExpect(content().contentType("text/csv; charset=utf-8"));
    }

    @Test
    void testImportCourses() throws Exception {
        String csvContent = "Title;Instructor Name;Instructor Email;Description;Duration (hours)\n" +
                "Imported Course;John Smith;john@example.com;Imported description of course;25\n";
        org.springframework.mock.web.MockMultipartFile file = new org.springframework.mock.web.MockMultipartFile(
                "file", "test.csv", "text/csv", csvContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/courses/import").file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        Mockito.verify(courseService, Mockito.times(1)).save(Mockito.any(Course.class));
    }
}
