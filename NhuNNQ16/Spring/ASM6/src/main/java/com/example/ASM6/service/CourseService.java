package com.example.ASM6.service;

import com.example.ASM6.model.Course;
import com.example.ASM6.repository.CourseRepository;
import com.example.ASM6.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final CategoryService categoryService;

    public Page<Course> getPublishedCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findByStatusOrderByCreatedAtDesc(2, pageable);
    }

    public Page<Course> getPublishedCoursesByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findPublishedByCategory(category, pageable);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public Course saveCourse(Course course) {
        Course saved = courseRepository.save(course);
        categoryService.rebuildCategoryFrequencies();
        return saved;
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.findById(id).ifPresent(course -> {
            reviewRepository.deleteByCourse(course);
            courseRepository.delete(course);
            categoryService.rebuildCategoryFrequencies();
        });
    }

    @Transactional
    public void updateStatus(Long id, Integer status) {
        courseRepository.findById(id).ifPresent(course -> {
            course.setStatus(status);
            courseRepository.save(course);
            categoryService.rebuildCategoryFrequencies();
        });
    }

    public long countCoursesByStatus(Integer status) {
        return courseRepository.countByStatus(status);
    }

    public long countAllCourses() {
        return courseRepository.count();
    }
}
