package com.example.lms_backend.service;

import com.example.lms_backend.entity.Course;
import com.example.lms_backend.entity.CourseId;
import com.example.lms_backend.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BaseServiceImplTest {

    private CourseRepository repository;
    private CourseServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(CourseRepository.class);
        service = new CourseServiceImpl(repository);
    }

    @Test
    void testSave() {
        Course course = new Course(new CourseId("CS101", LocalDate.now()), "Intro to CS", "CS", "Dr. Smith");
        when(repository.save(course)).thenReturn(course);

        Course result = service.save(course);
        assertNotNull(result);
        assertEquals("Intro to CS", result.getCourseName());
        verify(repository, times(1)).save(course);
    }

    @Test
    void testUpdate_Success() {
        CourseId id = new CourseId("CS101", LocalDate.of(2026, 6, 10));
        Course existing = new Course(id, "Intro to CS", "CS", "Dr. Smith");
        Course updatedData = new Course(null, "Intro to CS 2", "CS", "Dr. Jones"); // Id is null in input, should be populated via reflection

        when(repository.existsById(id)).thenReturn(true);
        when(repository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Course result = service.update(id, updatedData);
        assertNotNull(result);
        assertEquals(id, result.getId()); // ID was injected via reflection
        assertEquals("Intro to CS 2", result.getCourseName());
        assertEquals("Dr. Jones", result.getInstructor());
    }

    @Test
    void testUpdate_NotFound() {
        CourseId id = new CourseId("CS101", LocalDate.of(2026, 6, 10));
        Course updatedData = new Course(id, "Intro to CS 2", "CS", "Dr. Jones");

        when(repository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.update(id, updatedData));
    }
}
