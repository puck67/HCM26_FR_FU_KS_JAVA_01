package com.springcore.service;

import com.springcore.model.Course;

import java.util.List;

public interface CourseService {
    void saveCourses(List<Course> courses);
    List<Course> getCourses();
}
