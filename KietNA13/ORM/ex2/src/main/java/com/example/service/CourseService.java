package com.example.service;

import com.example.entity.Course;
import com.example.entity.Student;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    void createCourse(String title, int credit);

    void updateCourse(int id, String title, int credit);

    void deleteCourse(int id);

    Optional<Course> getCourseById(int id);

    List<Course> getAllCourses();

    List<Student> getStudentsOfCourse(int courseId);

    List<Course> findByMinCredit(int minCredit);
}
