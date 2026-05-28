package fa.training.service.impl;

import fa.training.dao.CourseDAO;
import fa.training.dao.EnrollmentDAO;
import fa.training.dao.QueryDAO;
import fa.training.dao.StudentDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.dao.impl.EnrollmentDAOImpl;
import fa.training.dao.impl.QueryDAOImpl;
import fa.training.dao.impl.StudentDAOImpl;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.service.StudentService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAOImpl();
    private final QueryDAO queryDAO = new QueryDAOImpl();

    @Override
    public void createStudent(String name, int age) {
        Student student = new Student(name, age);
        studentDAO.add(student);
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
            System.out.println("Student not found.");
        }
    }

    @Override
    public void deleteStudent(int id) {
        if (studentDAO.findById(id) != null) {
            studentDAO.delete(id);
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    @Override
    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        boolean success = enrollmentDAO.enrollStudentInCourse(studentId, courseId);
        if (success) {
            System.out.println("Student enrolled in course successfully.");
        } else {
            System.out.println("Student or Course not found, or enrollment failed.");
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        boolean success = enrollmentDAO.removeStudentFromCourse(studentId, courseId);
        if (success) {
            System.out.println("Student removed from course successfully.");
        } else {
            System.out.println("Student or Course not found, or removal failed.");
        }
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findById(studentId);
        if (student != null) {
            return student.getCourses();
        }
        return Collections.emptySet();
    }

    @Override
    public List<Student> getStudentsOlderThan(int age) {
        return queryDAO.findStudentsOlderThan(age);
    }

    @Override
    public List<Object[]> getStudentsWithCourseTitles() {
        return queryDAO.findStudentsWithCourseTitles();
    }

    @Override
    public List<Student> getStudentsByName(String name) {
        return queryDAO.findStudentsByName(name);
    }

    @Override
    public List<Student> getStudentsEnrolledInCourse(int courseId) {
        return queryDAO.findStudentsByCourseId(courseId);
    }
}
