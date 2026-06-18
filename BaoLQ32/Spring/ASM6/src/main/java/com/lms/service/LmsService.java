package com.lms.service;

import com.lms.model.*;
import com.lms.repository.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LmsService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LookupRepository lookupRepository;

    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    public String renderMarkdown(String markdown) {
        if (markdown == null) return "";
        return renderer.render(parser.parse(markdown));
    }

    // Courses
    public Page<Course> getRecentPublishedCourses(Pageable pageable) {
        return courseRepository.findByStatusOrderByCreatedAtDesc(2, pageable); // 2 = PUBLISHED
    }

    public Page<Course> getPublishedCoursesByCategory(String category, Pageable pageable) {
        return courseRepository.findByStatusAndCategoryOrderByCreatedAtDesc(2, category, pageable);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public Course saveCourse(Course course) {
        Course saved = courseRepository.save(course);
        recalculateCategoryFrequencies();
        return saved;
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
        recalculateCategoryFrequencies();
    }

    // Reviews
    public List<Review> getApprovedReviewsForCourse(Long courseId) {
        return reviewRepository.findByCourseIdAndStatusOrderByCreatedAtDesc(courseId, 2); // 2 = APPROVED
    }

    public List<Review> getRecentApprovedReviews() {
        return reviewRepository.findTop5ByStatusOrderByCreatedAtDesc(2);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Transactional
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    // Categories
    public List<Category> getCategoryCloud() {
        return categoryRepository.findAllByOrderByFrequencyDesc();
    }

    @Transactional
    public void recalculateCategoryFrequencies() {
        categoryRepository.deleteAll();
        List<Course> courses = courseRepository.findAll();
        Map<String, Integer> freqMap = new HashMap<>();

        for (Course course : courses) {
            // Count categories for PUBLISHED courses (status = 2)
            if (course.getStatus() == 2 && course.getCategory() != null) {
                String[] cats = course.getCategory().split(",");
                for (String cat : cats) {
                    String clean = cat.trim();
                    if (!clean.isEmpty()) {
                        freqMap.put(clean, freqMap.getOrDefault(clean, 0) + 1);
                    }
                }
            }
        }

        List<Category> categoryList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
            categoryList.add(new Category(entry.getKey(), entry.getValue()));
        }
        categoryRepository.saveAll(categoryList);
    }

    // Lookup
    public String getStatusLabel(String type, Integer code) {
        return lookupRepository.findByTypeAndCode(type, code)
                .map(Lookup::getLabel)
                .orElse("Unknown");
    }
}
