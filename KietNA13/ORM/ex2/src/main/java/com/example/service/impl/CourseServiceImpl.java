package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseServiceImpl implements CourseService {

    private static final int MIN_CREDIT = 1;
    private static final int MAX_CREDIT = 20;

    private final CourseDAO courseDAO;

    public CourseServiceImpl(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Tên khóa học không được để trống.");
        }
    }

    private void validateCredit(int credit) {
        if (credit < MIN_CREDIT || credit > MAX_CREDIT) {
            throw new IllegalArgumentException(
                    new StringBuilder("Số tín chỉ phải từ ").append(MIN_CREDIT).append(" đến ").append(MAX_CREDIT).append(".").toString()
            );
        }
    }

    @Override
    public void createCourse(String title, int credit) {
        validateTitle(title);
        validateCredit(credit);
        courseDAO.save(new Course(title, credit));
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        validateTitle(title);
        validateCredit(credit);
        Course existing = courseDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học ID=" + id));
        existing.setTitle(title);
        existing.setCredit(credit);
        courseDAO.update(existing);
    }

    @Override
    public void deleteCourse(int id) {
        courseDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học ID=" + id));
        courseDAO.delete(id);
    }

    @Override
    public Optional<Course> getCourseById(int id) {
        return courseDAO.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public List<Student> getStudentsOfCourse(int courseId) {
        Course course = courseDAO.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học ID=" + courseId));
        return new ArrayList<>(course.getStudents());
    }

    @Override
    public List<Course> findByMinCredit(int minCredit) {
        return courseDAO.findByMinCredit(minCredit);
    }
}
