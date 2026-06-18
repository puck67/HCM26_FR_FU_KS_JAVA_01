package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Course;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CourseRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.demo.service.base.GenericServiceImpl;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CourseServiceImpl extends GenericServiceImpl<Course, Long, CourseRepository>
        implements CourseService {

    private final CategoryRepository categoryRepository;

    public CourseServiceImpl(CourseRepository courseRepository,
                             CategoryRepository categoryRepository) {
        super(courseRepository);
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Course save(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (course.getCreatedDate() == null) {
            course.setCreatedDate(LocalDateTime.now());
        }
        if (course.getCategory() != null && !course.getCategory().trim().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            Arrays.stream(course.getCategory().split(","))
                  .map(String::trim)
                  .filter(c -> !c.isEmpty())
                  .forEach(c -> {
                      if (sb.length() > 0) sb.append(", ");
                      sb.append(c);
                  });
            course.setCategory(sb.toString());
        }
        Course saved = super.save(course);
        log.info(new StringBuilder().append("Saved course ID: ").append(saved.getId()).toString());
        recalculateCategoryFrequencies();
        return saved;
    }

    @Override
    @Transactional
    public Course saveCourse(Course course) {
        return save(course);
    }

    @Override
    public Course getCourseById(Long id) {
        return findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
        log.info(new StringBuilder().append("Deleted course ID: ").append(id).toString());
        recalculateCategoryFrequencies();
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        deleteById(id);
    }

    @Override
    public Page<Course> getPublishedCourses(Pageable pageable) {
        return repository.findByStatusOrderByCreatedDateDesc(2, pageable);
    }

    @Override
    public Page<Course> getCoursesByCategory(String category, Pageable pageable) {
        if (category == null || category.trim().isEmpty()) {
            return getPublishedCourses(pageable);
        }
        return repository.findByStatusAndCategory(2, category.trim(), pageable);
    }

    @Override
    public Page<Course> getAllCourses(Pageable pageable) {
        return repository.findAllByOrderByCreatedDateDesc(pageable);
    }

    @Override
    @Transactional
    public Course updateCourseStatus(Long id, Integer status) {
        if (id == null || status == null) {
            throw new IllegalArgumentException("ID and Status cannot be null");
        }
        if (status < 1 || status > 3) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
        Course course = findById(id);
        course.setStatus(status);
        Course updated = save(course);
        log.info(new StringBuilder().append("Updated course ID: ").append(id)
                .append(" to status: ").append(status).toString());
        recalculateCategoryFrequencies();
        return updated;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByFrequencyDesc();
    }

    @Override
    @Transactional
    public void recalculateCategoryFrequencies() {
        categoryRepository.deleteAll();
        Map<String, Integer> freqMap = new HashMap<>();
        repository.findAll().stream()
                  .filter(c -> Integer.valueOf(2).equals(c.getStatus()))
                  .filter(c -> c.getCategory() != null && !c.getCategory().trim().isEmpty())
                  .forEach(c -> Arrays.stream(c.getCategory().split(","))
                          .map(String::trim)
                          .filter(s -> !s.isEmpty())
                          .forEach(s -> freqMap.put(s, freqMap.getOrDefault(s, 0) + 1)));
        freqMap.forEach((name, freq) ->
                categoryRepository.save(Category.builder().name(name).frequency(freq).build()));
        log.info("Recalculated category frequencies.");
    }

    @Override
    public long countCoursesByStatus(Integer status) {
        return status == null ? count() : repository.countByStatus(status);
    }
}
