package com.example.lms_backend.controller;

import com.example.lms_backend.entity.Course;
import com.example.lms_backend.entity.CourseId;
import com.example.lms_backend.entity.Lesson;
import com.example.lms_backend.service.CourseService;
import com.example.lms_backend.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseControllerTest {

    private MockMvc mockMvc;
    private CourseService courseService;
    private LessonService lessonService;

    @BeforeEach
    void setUp() {
        courseService = mock(CourseService.class);
        lessonService = mock(LessonService.class);
        CourseController controller = new CourseController(courseService, lessonService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetByCompositeId_Found() throws Exception {
        CourseId id = new CourseId("CS101", LocalDate.of(2026, 6, 10));
        Course course = new Course(id, "CS Intro", "CS", "Dr. Smith");

        when(courseService.findById(id)).thenReturn(Optional.of(course));

        mockMvc.perform(get("/api/courses/CS101/2026-06-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName").value("CS Intro"))
                .andExpect(jsonPath("$.id.courseCode").value("CS101"));
    }

    @Test
    void testGetByCompositeId_NotFound() throws Exception {
        CourseId id = new CourseId("CS101", LocalDate.of(2026, 6, 10));
        when(courseService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/courses/CS101/2026-06-10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateByCompositeId_Success() throws Exception {
        CourseId id = new CourseId("CS101", LocalDate.of(2026, 6, 10));
        Course course = new Course(id, "CS Intro Updated", "CS", "Dr. Jones");

        when(courseService.update(eq(id), any(Course.class))).thenReturn(course);

        mockMvc.perform(put("/api/courses/CS101/2026-06-10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseName\":\"CS Intro Updated\",\"category\":\"CS\",\"instructor\":\"Dr. Jones\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName").value("CS Intro Updated"));
    }

    @Test
    void testGetLessonsForCourse_Success() throws Exception {
        CourseId id = new CourseId("CS101", LocalDate.of(2026, 6, 10));
        Course course = new Course(id, "CS Intro", "CS", "Dr. Smith");
        Lesson lesson = new Lesson(1L, "Intro to OOP", 45, "Video", "Active", course);

        when(courseService.findById(id)).thenReturn(Optional.of(course));
        when(lessonService.findByCourseId(id)).thenReturn(Collections.singletonList(lesson));

        mockMvc.perform(get("/api/courses/CS101/2026-06-10/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lessonName").value("Intro to OOP"))
                .andExpect(jsonPath("$[0].duration").value(45));
    }
}
