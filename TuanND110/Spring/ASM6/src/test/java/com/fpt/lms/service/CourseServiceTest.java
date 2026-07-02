package com.fpt.lms.service;

import com.fpt.lms.dto.CourseFormDTO;
import com.fpt.lms.entity.Category;
import com.fpt.lms.entity.Course;
import com.fpt.lms.entity.CourseStatus;
import com.fpt.lms.entity.Instructor;
import com.fpt.lms.repository.CategoryRepository;
import com.fpt.lms.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CourseService.
 * Uses Mockito to isolate from database.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService Unit Tests")
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CourseService courseService;

    private Instructor testInstructor;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testInstructor = Instructor.builder()
            .id(1L)
            .username("instructor1")
            .fullName("Test Instructor")
            .build();

        testCourse = Course.builder()
            .id(1L)
            .title("Spring Boot Basics")
            .description("Learn Spring Boot")
            .content("# Introduction\nSpring Boot content here.")
            .status(CourseStatus.DRAFT.getValue())
            .category("Java,Spring")
            .instructor(testInstructor)
            .build();
    }

    // ── findByIdOrThrow ────────────────────────────────────────────────────

    @Test
    @DisplayName("findByIdOrThrow - returns course when found")
    void findByIdOrThrow_returnsWhenFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        Course result = courseService.findByIdOrThrow(1L);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Spring Boot Basics");
    }

    @Test
    @DisplayName("findByIdOrThrow - throws EntityNotFoundException when not found")
    void findByIdOrThrow_throwsWhenNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.findByIdOrThrow(99L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("99");
    }

    // ── saveFromDTO ────────────────────────────────────────────────────────

    @Test
    @DisplayName("saveFromDTO - creates new course from DTO")
    void saveFromDTO_createsNewCourse() {
        CourseFormDTO dto = CourseFormDTO.builder()
            .title("New Course")
            .description("Description")
            .content("Content")
            .category("Java")
            .build();

        when(courseRepository.save(any(Course.class))).thenAnswer(inv -> {
            Course c = inv.getArgument(0);
            c.setId(10L);
            return c;
        });
        when(courseRepository.findCategoryStringsByStatus(anyInt())).thenReturn(List.of("Java"));

        Course saved = courseService.saveFromDTO(dto, testInstructor);

        assertThat(saved).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("New Course");
        assertThat(saved.getInstructor()).isEqualTo(testInstructor);
        assertThat(saved.getStatus()).isEqualTo(CourseStatus.DRAFT.getValue());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("saveFromDTO - status defaults to DRAFT when not provided")
    void saveFromDTO_defaultsStatusToDraft() {
        CourseFormDTO dto = CourseFormDTO.builder()
            .title("Draft Course")
            .description("Desc")
            .content("Content")
            .build();

        when(courseRepository.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));
        when(courseRepository.findCategoryStringsByStatus(anyInt())).thenReturn(List.of());

        Course saved = courseService.saveFromDTO(dto, testInstructor);

        assertThat(saved.getStatus()).isEqualTo(CourseStatus.DRAFT.getValue());
    }

    // ── updateStatus ───────────────────────────────────────────────────────

    @Test
    @DisplayName("updateStatus - updates to PUBLISHED")
    void updateStatus_setsPublished() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);
        when(courseRepository.findCategoryStringsByStatus(anyInt())).thenReturn(List.of("Java,Spring"));

        courseService.updateStatus(1L, CourseStatus.PUBLISHED);

        assertThat(testCourse.getStatus()).isEqualTo(CourseStatus.PUBLISHED.getValue());
        verify(courseRepository, times(1)).save(testCourse);
    }

    @Test
    @DisplayName("updateStatus - throws when course not found")
    void updateStatus_throwsWhenNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.updateStatus(99L, CourseStatus.PUBLISHED))
            .isInstanceOf(EntityNotFoundException.class);
    }

    // ── delete ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - calls deleteById and rebuilds category stats")
    void delete_callsRepositoryAndRebuildsStats() {
        when(courseRepository.findCategoryStringsByStatus(anyInt())).thenReturn(List.of());

        courseService.delete(1L);

        verify(courseRepository, times(1)).deleteById(1L);
        verify(categoryRepository, times(1)).deleteAll();
    }

    // ── rebuildCategoryStats ───────────────────────────────────────────────

    @Test
    @DisplayName("rebuildCategoryStats - correctly counts comma-separated categories")
    void rebuildCategoryStats_countsCategoriesCorrectly() {
        when(courseRepository.findCategoryStringsByStatus(CourseStatus.PUBLISHED.getValue()))
            .thenReturn(List.of("Java,Spring", "Java,Docker", "Python"));

        courseService.rebuildCategoryStats();

        // Should save 4 categories: Java(2), Spring(1), Docker(1), Python(1)
        verify(categoryRepository, times(4)).save(any(Category.class));
    }

    @Test
    @DisplayName("rebuildCategoryStats - handles empty categories gracefully")
    void rebuildCategoryStats_handlesEmpty() {
        when(courseRepository.findCategoryStringsByStatus(anyInt())).thenReturn(List.of());

        assertThatNoException().isThrownBy(() -> courseService.rebuildCategoryStats());
        verify(categoryRepository, times(0)).save(any(Category.class));
    }
}
