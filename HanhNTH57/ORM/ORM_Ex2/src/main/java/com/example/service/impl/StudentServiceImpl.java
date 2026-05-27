package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;
import java.util.List;
import java.util.Set;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createStudent(String name, int age) {
        Student student = new Student(name, age);
        studentDAO.save(student);
        System.out.println("Student created successfully.");
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        Student student = studentDAO.findById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
            studentDAO.update(student);
            System.out.println("Student updated successfully.");
        } else {
            System.err.println("Student not found.");
        }
    }

    @Override
    public void deleteStudent(int id) {
        studentDAO.delete(id);
        System.out.println("Student deleted successfully.");
    }

    @Override
    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        
        if (student != null && course != null) {
            student.addCourse(course);
            studentDAO.update(student);
            System.out.println("Student enrolled in course successfully.");
        } else {
            System.err.println("Student or Course not found.");
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        
        if (student != null && course != null) {
            student.removeCourse(course);
            studentDAO.update(student);
            System.out.println("Student removed from course successfully.");
        } else {
            System.err.println("Student or Course not found.");
        }
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findById(studentId);
        return (student != null) ? student.getCourses() : null;
    }

    @Override
    public List<Student> getStudentsOlderThan(int age) {
        return studentDAO.findByAgeGreaterThan(age);
    }

    @Override
    public List<Student> getStudentsByName(String name) {
        return studentDAO.findByName(name);
    }

    @Override
    public void displayStudentsWithCourseTitles() {
        List<Object[]> results = studentDAO.findAllStudentsWithCourseTitles();
        StringBuilder sb = new StringBuilder();
        sb.append("--- Students and their Courses ---\n");
        
        // Using Stream to process and build the report
        results.stream().forEach(row -> {
            sb.append("Student: ").append(row[0])
              .append(" | Course: ").append(row[1])
              .append("\n");
        });
        
        System.out.print(sb.toString());
    }

    @Override
    public List<Student> getStudentsByCourse(int courseId) {
        return studentDAO.findByCourseId(courseId);
    }

    @Override
    public List<Student> getAllStudentsPaginated(int pageNumber, int pageSize) {
        return studentDAO.findAllPaginated(pageNumber, pageSize);
    }

    @Override
    public List<Student> getStudentsNotEnrolledInAnyCourse() {
        return studentDAO.findStudentsNotEnrolled();
    }
}
