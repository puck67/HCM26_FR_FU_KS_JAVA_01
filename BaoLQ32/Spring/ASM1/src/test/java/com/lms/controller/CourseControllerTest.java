package com.lms.controller;

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
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/courses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_course"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    public void testCreateCourseValidationFails() throws Exception {
        mockMvc.perform(post("/courses/create")
                .param("title", "")
                .param("instructorName", "")
                .param("instructorEmail", "")
                .param("durationHours", "")
                .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("create_course"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("course", "title", "instructorName", "instructorEmail", "durationHours", "description"));
    }

    @Test
    public void testCreateCourseSuccess() throws Exception {
        mockMvc.perform(post("/courses/create")
                .param("title", "Advanced Java Spring Boot")
                .param("instructorName", "Dr. Jane Smith")
                .param("instructorEmail", "janesmith@example.com")
                .param("durationHours", "45")
                .param("description", "This course is a comprehensive guide to Java Spring Boot validation and MVC patterns."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/success"));
    }
}
