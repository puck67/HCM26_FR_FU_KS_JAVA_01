package com.example.demo;

import com.example.demo.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void testShowCreateForm() throws Exception {
		mockMvc.perform(get("/courses/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("create_course"))
				.andExpect(model().attributeExists("course"));
	}

	@Test
	void testCreateCourseSuccess_HappyPath() throws Exception {
		mockMvc.perform(post("/courses/create")
						.param("title", "Spring Boot Basics")
						.param("instructorName", "John Doe")
						.param("instructorEmail", "john.doe@example.com")
						.param("description", "Learn Spring Boot from scratch step by step.")
						.param("durationHours", "30"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/courses/success"));
	}

	@Test
	void testCreateCourseFail_EmptyFields() throws Exception {
		mockMvc.perform(post("/courses/create")
						.param("title", "")
						.param("instructorName", "")
						.param("instructorEmail", "")
						.param("description", "")
						.param("durationHours", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("create_course"))
				.andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrors("course", "title"))
				.andExpect(model().attributeHasFieldErrors("course", "instructorName"))
				.andExpect(model().attributeHasFieldErrors("course", "instructorEmail"))
				.andExpect(model().attributeHasFieldErrors("course", "description"))
				.andExpect(model().attributeHasFieldErrors("course", "durationHours"));
	}

	@Test
	void testCreateCourseFail_ConstraintsViolated() throws Exception {
		mockMvc.perform(post("/courses/create")
						.param("title", "Java") // less than 5 characters
						.param("instructorName", "J") // less than 2 characters
						.param("instructorEmail", "invalid-email") // invalid email
						.param("description", "Short") // less than 10 characters
						.param("durationHours", "0")) // less than 1
				.andExpect(status().isOk())
				.andExpect(view().name("create_course"))
				.andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrors("course", "title"))
				.andExpect(model().attributeHasFieldErrors("course", "instructorName"))
				.andExpect(model().attributeHasFieldErrors("course", "instructorEmail"))
				.andExpect(model().attributeHasFieldErrors("course", "description"))
				.andExpect(model().attributeHasFieldErrors("course", "durationHours"));
	}

	@Test
	void testCreateCourseFail_TypeMismatch() throws Exception {
		mockMvc.perform(post("/courses/create")
						.param("title", "Spring Boot Basics")
						.param("instructorName", "John Doe")
						.param("instructorEmail", "john.doe@example.com")
						.param("description", "Learn Spring Boot from scratch step by step.")
						.param("durationHours", "abc")) // String input for Integer field
				.andExpect(status().isOk())
				.andExpect(view().name("create_course"))
				.andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrors("course", "durationHours"));
	}
}

