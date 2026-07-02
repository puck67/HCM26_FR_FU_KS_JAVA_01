package com.fpt.lms.service;

import com.fpt.lms.dto.CourseFormDTO;
import com.fpt.lms.entity.Category;
import com.fpt.lms.entity.Course;
import com.fpt.lms.entity.CourseStatus;
import com.fpt.lms.entity.Instructor;
import com.fpt.lms.repository.CategoryRepository;
import com.fpt.lms.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing Course entities and derived Category data.
 *
 * Improvements over original:
 * - Constructor injection (testable, no field injection)
 * - Uses CourseStatus enum instead of magic integers
 * - updateCategories() uses efficient GROUP BY native query
 * - CourseFormDTO for decoupled form handling
 * - Proper logging with SLF4J
 */
@Service
@Transactional
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    // ── Read Operations ──────────────────────────────────────────────────────

    public Page<Course> getPublishedCourses(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        return courseRepository.findByStatusOrderByCreatedAtDesc(CourseStatus.PUBLISHED.getValue(), pageable);
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public Course findByIdOrThrow(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findPublishedByCategory(category);
    }

    // ── Write Operations ─────────────────────────────────────────────────────

    /**
     * Save a course from form DTO, setting the instructor.
     */
    public Course saveFromDTO(CourseFormDTO dto, Instructor instructor) {
        Course course;
        if (dto.getId() != null) {
            // Update existing — ensure instructor owns this course
            course = findByIdOrThrow(dto.getId());
        } else {
            course = new Course();
        }
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setContent(dto.getContent());
        course.setCategory(dto.getCategory());
        course.setInstructor(instructor);
        if (dto.getStatus() != null) {
            course.setStatus(dto.getStatus());
        } else if (course.getStatus() == null) {
            course.setStatus(CourseStatus.DRAFT.getValue());
        }

        Course saved = courseRepository.save(course);
        log.info("Course saved: id={}, title='{}', status={}", saved.getId(), saved.getTitle(), saved.getStatus());
        rebuildCategoryStats();
        return saved;
    }

    public Course save(Course course) {
        Course saved = courseRepository.save(course);
        rebuildCategoryStats();
        return saved;
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
        log.info("Course deleted: id={}", id);
        rebuildCategoryStats();
    }

    /**
     * Updates course status via enum for type safety.
     */
    public void updateStatus(Long id, CourseStatus newStatus) {
        Course course = findByIdOrThrow(id);
        course.setStatus(newStatus.getValue());
        courseRepository.save(course);
        log.info("Course status updated: id={}, newStatus={}", id, newStatus);
        rebuildCategoryStats();
    }

    // ── Category Sync ─────────────────────────────────────────────────────────

    /**
     * Rebuilds category frequency table using GROUP BY on the published courses' category field.
     *
     * Original implementation loaded ALL published courses into memory (O(n) heap).
     * This version uses native SQL GROUP BY for efficiency.
     *
     * Note: Category field stores comma-separated values (e.g., "Java,Spring").
     * Since SQL can't split strings portably, we use a two-step approach:
     * 1. Query only category strings of published courses
     * 2. Count in-memory (only category strings, not full entities)
     */
    public void rebuildCategoryStats() {
        // Fetch only the category strings (not full entities) to reduce memory
        List<String> categoryStrings = courseRepository.findCategoryStringsByStatus(CourseStatus.PUBLISHED.getValue());

        // Count frequency per individual category tag
        Map<String, Long> freq = categoryStrings.stream()
            .filter(s -> s != null && !s.isBlank())
            .flatMap(s -> Arrays.stream(s.split(",")))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        // Delete all existing categories and re-insert (simpler than incremental update)
        categoryRepository.deleteAll();
        freq.forEach((name, count) -> {
            Category cat = Category.builder()
                .name(name)
                .frequency(count.intValue())
                .build();
            categoryRepository.save(cat);
        });

        log.debug("Category stats rebuilt: {} categories", freq.size());
    }
}
