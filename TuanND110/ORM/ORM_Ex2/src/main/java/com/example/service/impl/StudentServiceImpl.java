package com.example.service.impl;
import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;
import java.util.ArrayList;
import java.util.List;
public class StudentServiceImpl implements StudentService {
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    // Default constructor for production / existing callers
    public StudentServiceImpl() {
        this(new StudentDAOImpl(), new CourseDAOImpl());
    }

    // Constructor injection for testability
    public StudentServiceImpl(StudentDAO studentDAO, CourseDAO courseDAO) {
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }
    @Override
    public Student createStudent(String name, int age) {
        Student student = new Student(name, age);
        studentDAO.save(student);
        return student;
    }
    @Override
    public Student updateStudent(int id, String name, int age) {
        Student student = studentDAO.findById(id);
        if (student == null) {
            return null;
        }
        student.setName(name);
        student.setAge(age);
        studentDAO.update(student);
        return studentDAO.findById(id);
    }
    @Override
    public boolean deleteStudent(int id) {
        Student student = studentDAO.findById(id);
        if (student == null) {
            return false;
        }
        studentDAO.delete(id);
        return true;
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
    public boolean enrollStudentInCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        if (student == null || course == null) {
            return false;
        }
        if (student.getCourses().contains(course)) {
            return false;
        }
        student.addCourse(course);
        studentDAO.update(student);
        return true;
    }
    @Override
    public boolean removeStudentFromCourse(int studentId, int courseId) {
        Student student = studentDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        if (student == null || course == null) {
            return false;
        }
        if (!student.getCourses().contains(course)) {
            return false;
        }
        student.removeCourse(course);
        studentDAO.update(student);
        return true;
    }
    @Override
    public List<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(student.getCourses());
    }
    @Override
    public List<Student> findStudentsOlderThan(int age) {
        return studentDAO.findStudentsOlderThan(age);
    }
    @Override
    public List<Student> findStudentsByName(String name) {
        return studentDAO.findStudentsByName(name);
    }
    @Override
    public List<Object[]> listStudentsWithCourses() {
        return studentDAO.listStudentsWithCourseTitles();
    }
    @Override
    public List<Student> findStudentsEnrolledInCourse(int courseId) {
        return studentDAO.findStudentsByCourseId(courseId);
    }
}
