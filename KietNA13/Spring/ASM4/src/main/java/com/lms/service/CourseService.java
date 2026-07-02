package com.lms.service;

import com.lms.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);
    private final Map<String, Course> courseStore = new ConcurrentHashMap<>();

    public void addNewCourse(Course course) {
        if (course == null) {
            log.warn("addNewCourse called with a null Course — operation skipped.");
            return;
        }
        if (course.getCourseId() == null || course.getCourseId().isBlank()) {
            log.warn("addNewCourse called with a null/blank courseId — operation skipped.");
            return;
        }

        if (courseStore.containsKey(course.getCourseId())) {
            log.warn("Course already exists and was not overwritten: {}", course.getCourseId());
            return;
        }

        courseStore.put(course.getCourseId(), course);
        log.info("Added course: {}", course);
    }

    public List<Course> fetchAllCourses() {
        return courseStore.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public Optional<Course> findCourseById(String courseId) {
        if (courseId == null || courseId.isBlank()) {
            log.warn("findCourseById called with a null/blank courseId — returning empty.");
            return Optional.empty();
        }
        return Optional.ofNullable(courseStore.get(courseId));
    }

    public void updateCourseById(String courseId, Course updatedCourse) {
        if (courseId == null || courseId.isBlank()) {
            log.warn("updateCourseById called with a null/blank courseId — operation skipped.");
            return;
        }
        if (updatedCourse == null) {
            log.warn("updateCourseById called with a null updatedCourse — operation skipped.");
            return;
        }

        if (!courseStore.containsKey(courseId)) {
            log.warn("Course not found for update: {}", courseId);
            return;
        }

        courseStore.put(courseId, updatedCourse);
        log.info("Updated course: {}", updatedCourse);
    }

    public void removeCourseById(String courseId) {
        if (courseId == null || courseId.isBlank()) {
            log.warn("removeCourseById called with a null/blank courseId — operation skipped.");
            return;
        }

        if (courseStore.remove(courseId) != null) {
            log.info("Deleted course with id: {}", courseId);
        } else {
            log.warn("Course not found for deletion: {}", courseId);
        }
    }

    public List<Course> fetchCoursesByMinDuration(int minHours) {
        return courseStore.values()
                .stream()
                .filter(c -> c.getDurationHours() >= minHours)
                .collect(Collectors.toUnmodifiableList());
    }
}
