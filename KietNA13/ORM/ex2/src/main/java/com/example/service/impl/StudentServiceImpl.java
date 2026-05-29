package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentServiceImpl implements StudentService {

    private static final int MIN_AGE  = 1;
    private static final int MAX_AGE  = 150;

    private final StudentDAO studentDAO;
    private final CourseDAO  courseDAO;

    public StudentServiceImpl(StudentDAO studentDAO, CourseDAO courseDAO) {
        this.studentDAO = studentDAO;
        this.courseDAO  = courseDAO;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên sinh viên không được để trống.");
        }
    }

    private void validateAge(int age) {
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new IllegalArgumentException(
                    new StringBuilder("Tuổi phải từ ").append(MIN_AGE).append(" đến ").append(MAX_AGE).append(".").toString()
            );
        }
    }

    @Override
    public void createStudent(String name, int age) {
        validateName(name);
        validateAge(age);
        studentDAO.save(new Student(name, age));
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        validateName(name);
        validateAge(age);
        Student existing = studentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên ID=" + id));
        existing.setName(name);
        existing.setAge(age);
        studentDAO.update(existing);
    }

    @Override
    public void deleteStudent(int id) {
        studentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên ID=" + id));
        studentDAO.delete(id);
    }

    @Override
    public Optional<Student> getStudentById(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên ID=" + studentId));
        Course course = courseDAO.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học ID=" + courseId));

        boolean alreadyEnrolled = student.getCourses().stream()
                .anyMatch(c -> c.getId() == courseId);
        if (alreadyEnrolled) {
            throw new IllegalStateException("Sinh viên đã đăng ký khóa học này rồi.");
        }

        student.getCourses().add(course);
        studentDAO.update(student);
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên ID=" + studentId));
        courseDAO.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học ID=" + courseId));

        boolean removed = student.getCourses().removeIf(c -> c.getId() == courseId);
        if (!removed) {
            throw new IllegalStateException("Sinh viên chưa đăng ký khóa học này.");
        }
        studentDAO.update(student);
    }

    @Override
    public List<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên ID=" + studentId));
        return new ArrayList<>(student.getCourses());
    }

    @Override
    public List<Student> findOlderThan(int age) {
        return studentDAO.findOlderThan(age);
    }

    @Override
    public List<Student> findByName(String name) {
        return studentDAO.findByName(name);
    }

    @Override
    public List<Object[]> findAllWithCourses() {
        return studentDAO.findAllWithCourses();
    }

    @Override
    public List<Student> findByCourseId(int courseId) {
        return studentDAO.findByCourseId(courseId);
    }

    @Override
    public List<Object[]> countPerCourse() {
        return studentDAO.countPerCourse();
    }
}
