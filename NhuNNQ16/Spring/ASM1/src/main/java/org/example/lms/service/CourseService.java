package org.example.lms.service;

import org.example.lms.model.Course;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CourseService {
    private final List<Course> courses = Collections.synchronizedList(new ArrayList<>());

    public void save(Course course) {

        courses.add(course);
    }

    public List<Course> getAllCourses() {

        return new ArrayList<>(courses);
    }

    public Course getLatestCourse() {
        if (courses.isEmpty()) {
            return null;
        }
        return courses.get(courses.size() - 1);
    }
}
