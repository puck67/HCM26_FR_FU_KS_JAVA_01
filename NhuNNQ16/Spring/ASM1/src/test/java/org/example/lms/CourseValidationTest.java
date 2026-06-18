package org.example.lms;

import org.example.lms.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CourseValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/courses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_course"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    void testCreateCourseSuccess() throws Exception {
        mockMvc.perform(post("/courses/create")
                        .param("title", "Java Spring Boot Masterclass")
                        .param("instructorName", "Jane Doe")
                        .param("instructorEmail", "jane.doe@example.com")
                        .param("description", "A comprehensive course covering everything from basics to advanced features of Spring Boot.")
                        .param("durationHours", "45"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/success"));
    }

    @Test
    void testCreateCourseValidationFailure() throws Exception {
        // Invalid title (< 5 chars), invalid email, invalid description, invalid duration
        mockMvc.perform(post("/courses/create")
                        .param("title", "Java")
                        .param("instructorName", "J")
                        .param("instructorEmail", "invalid-email")
                        .param("description", "Too short")
                        .param("durationHours", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_course"))
                .andExpect(model().attributeHasFieldErrors("course", "title"))
                .andExpect(model().attributeHasFieldErrors("course", "instructorName"))
                .andExpect(model().attributeHasFieldErrors("course", "instructorEmail"))
                .andExpect(model().attributeHasFieldErrors("course", "description"))
                .andExpect(model().attributeHasFieldErrors("course", "durationHours"));
    }

    @Test
    void testShowSuccessPage() throws Exception {
        mockMvc.perform(get("/courses/success"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_success"));
    }
}
