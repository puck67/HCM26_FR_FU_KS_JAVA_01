package com.example.ex2.service;

import com.example.ex2.entity.Course;
import com.example.ex2.entity.Student;

import java.util.List;

public interface CourseService {

    List<Course> getAllCourses();

    Course saveCourse(Course course);

    Course updateCourse(Course course);

    void deleteCourse(int id);

    Course getCourse(int id);

    List<Course> findCoursesWithCreditGreaterThan(int credit);

    List<Object[]> getStudentCountPerCourse();

    List<Student> getStudentsOfCourse(int courseId);
}
