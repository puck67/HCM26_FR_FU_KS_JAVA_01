package fa.training.service.impl;

import fa.training.service.*;
import fa.training.dao.*;
import fa.training.dao.impl.*;
import fa.training.entity.*;
import java.util.*;

public class StudentServiceImpl implements StudentService {

    private StudentDAO studentDAO = new StudentDAOImpl();
    private CourseDAO courseDAO = new CourseDAOImpl();

    public void createStudent(String name, int age) {
        Student s = new Student();
        s.setName(name);
        s.setAge(age);
        studentDAO.save(s);
    }

    public void updateStudent(int id, String name, int age) {
        Student s = studentDAO.findById(id);
        if (s != null) {
            s.setName(name);
            s.setAge(age);
            studentDAO.update(s);
        }
    }

    public void deleteStudent(int id) {
        studentDAO.delete(id);
    }

    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {

        Student s = studentDAO.findById(studentId);

        Course c = courseDAO.findById(courseId);

        if (s == null) {

            System.out.println("Student not found");

            return;
        }

        if (c == null) {

            System.out.println("Course not found");

            return;
        }

        // add course to student
        s.getCourses().add(c);

        // optional: sync both sides in memory
        c.getStudents().add(s);

        // IMPORTANT:
        // update OWNING SIDE
        studentDAO.update(s);

        System.out.println("Enrollment successful");
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        Student s = studentDAO.findById(studentId);
        Course c = courseDAO.findById(courseId);

        s.getCourses().remove(c);
        c.getStudents().remove(s);

        studentDAO.update(s);
        courseDAO.update(c);

        System.out.println("Remove successfully");
    }

    public Set<Course> getCoursesOfStudent(int studentId) {
        return studentDAO.findById(studentId).getCourses();
    }
}