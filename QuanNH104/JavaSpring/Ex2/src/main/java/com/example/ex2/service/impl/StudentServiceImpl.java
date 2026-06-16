package com.example.ex2.service.impl;

import com.example.ex2.entity.Course;
import com.example.ex2.entity.Student;
import com.example.ex2.repository.CourseRepository;
import com.example.ex2.repository.StudentRepository;
import com.example.ex2.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudent(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findStudentsOlderThan(int age) {
        return studentRepository.findStudentsOlderThan(age);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return studentRepository.findAll();
        }
        return studentRepository.findByNameContainingIgnoreCase(name.trim());
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + courseId + " not found"));
        
        student.addCourse(course);
        studentRepository.save(student);
    }

    @Override
    public void unenrollStudentFromCourse(int studentId, int courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + courseId + " not found"));
        
        student.removeCourse(course);
        studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getCoursesOfStudent(int studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " not found"));
        return new ArrayList<>(student.getCourses());
    }
}
