package com.example.service.impl;

import com.example.dao.StudentDAO;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Student;
import com.example.service.StudentService;

import java.util.List;

/**
 * StudentServiceImpl delegates all operations to StudentDAO.
 * Business logic (e.g. validation, pagination offset calculation) lives here.
 */
public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public void createStudent(String name, int age) {
        studentDAO.save(new Student(name, age));
    }

    @Override
    public boolean updateStudent(int id, String name, int age) {
        Student student = studentDAO.findById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
            studentDAO.update(student);
            return true;
        }
        System.out.println("Student with ID " + id + " not found.");
        return false;
    }

    @Override
    public boolean deleteStudent(int id) {
        Student student = studentDAO.findById(id);
        if (student != null) {
            studentDAO.delete(id);
            return true;
        }
        System.out.println("Student with ID " + id + " not found.");
        return false;
    }

    @Override
    public Student getStudent(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public List<Student> getAllStudentsPaginated(int page, int size) {
        int offset = (page - 1) * size;
        return studentDAO.findAllPaginated(offset, size);
    }

    @Override
    public List<Student> getStudentsOlderThan(int age) {
        return studentDAO.findOlderThan(age);
    }

    @Override
    public List<Object[]> getStudentsAndCourses() {
        return studentDAO.findStudentsAndCourses();
    }

    @Override
    public List<Student> getStudentsByName(String name) {
        return studentDAO.findByName(name);
    }

    @Override
    public List<Student> getUnenrolledStudents() {
        return studentDAO.findUnenrolledStudents();
    }
}
