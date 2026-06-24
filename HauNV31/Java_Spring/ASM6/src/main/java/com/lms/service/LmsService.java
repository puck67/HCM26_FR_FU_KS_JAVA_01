package com.lms.service;

import com.lms.model.Category;
import com.lms.model.Course;
import com.lms.model.Instructor;
import com.lms.model.Lookup;
import com.lms.model.Review;
import com.lms.repository.CategoryRepository;
import com.lms.repository.CourseRepository;
import com.lms.repository.InstructorRepository;
import com.lms.repository.LookupRepository;
import com.lms.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class LmsService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LookupRepository lookupRepository;

    public Page<Course> getPublishedCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findByStatusOrderByIdDesc(2, pageable);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course saveCourse(Course course) {
        Course saved = courseRepository.save(course);
        syncCategories();
        return saved;
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
        syncCategories();
    }

    public List<Course> getPublishedCoursesByCategory(String category) {
        return courseRepository.findPublishedByCategory(2, category);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public void syncCategories() {
        List<Course> courses = courseRepository.findAll();
        Map<String, Integer> freqMap = new HashMap<>();

        courses.forEach(c -> {
            if (c.getCategory() != null && !c.getCategory().trim().isEmpty()) {
                Arrays.stream(c.getCategory().split(","))
                        .map(String::trim)
                        .filter(tag -> !tag.isEmpty())
                        .forEach(tag -> freqMap.put(tag, freqMap.getOrDefault(tag, 0) + 1));
            }
        });

        categoryRepository.deleteAllInBatch();
        freqMap.forEach((name, freq) -> categoryRepository.save(new Category(name, freq)));
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getRecentReviews() {
        return reviewRepository.findTop5ByStatusOrderByIdDesc(2);
    }

    public List<Review> getReviewsByCourseAndStatus(Long courseId, Integer status) {
        return reviewRepository.findByCourseIdAndStatusOrderByIdDesc(courseId, status);
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public void approveReview(Long reviewId) {
        reviewRepository.findById(reviewId).ifPresent(r -> {
            r.setStatus(2);
            reviewRepository.save(r);
        });
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public Instructor getInstructorByUsername(String username) {
        return instructorRepository.findByUsername(username).orElse(null);
    }

    public boolean authenticateInstructor(String username, String password) {
        return instructorRepository.findByUsername(username)
                .map(inst -> inst.getPassword().equals(password))
                .orElse(false);
    }

    public String getLookupValue(String type, String code) {
        return lookupRepository.findByTypeAndCode(type, code)
                .map(Lookup::getValue)
                .orElse(code);
    }

    public List<Lookup> getLookupsByType(String type) {
        return lookupRepository.findByType(type);
    }
}
