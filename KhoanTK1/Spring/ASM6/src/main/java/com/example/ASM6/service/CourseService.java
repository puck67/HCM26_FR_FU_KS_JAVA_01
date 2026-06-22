package com.example.ASM6.service;

import com.example.ASM6.model.Course;
import com.example.ASM6.repository.CourseRepository;
import com.example.ASM6.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepo;
    private final ReviewRepository reviewRepo;
    private final CategoryService categorySrv;

    public CourseService(CourseRepository courseRepo, ReviewRepository reviewRepo, CategoryService categorySrv) {
        this.courseRepo = courseRepo;
        this.reviewRepo = reviewRepo;
        this.categorySrv = categorySrv;
    }

    public Page<Course> getPublishedCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepo.findByStatusOrderByCreatedAtDesc(2, pageable);
    }

    public Page<Course> getPublishedCoursesByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepo.findPublishedByCategory(category, pageable);
    }

    public List<Course> getAllCourses() {
        return courseRepo.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepo.findById(id);
    }

    @Transactional
    public Course saveCourse(Course course) {
        Course saved = courseRepo.save(course);
        categorySrv.rebuildCategoryFrequencies();
        return saved;
    }

    @Transactional
    public void deleteCourse(Long id) {
        Optional<Course> courseOpt = courseRepo.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            reviewRepo.deleteByCourse(course);
            courseRepo.delete(course);
            categorySrv.rebuildCategoryFrequencies();
        }
    }

    @Transactional
    public void updateStatus(Long id, Integer status) {
        Optional<Course> courseOpt = courseRepo.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setStatus(status);
            courseRepo.save(course);
            categorySrv.rebuildCategoryFrequencies();
        }
    }

    public long countCoursesByStatus(Integer status) {
        return courseRepo.countByStatus(status);
    }

    public long countAllCourses() {
        return courseRepo.count();
    }
}
