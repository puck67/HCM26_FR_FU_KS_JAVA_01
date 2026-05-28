package com.example.service.impl;
import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import java.util.ArrayList;
import java.util.List;
public class CourseServiceImpl implements CourseService {
    private final CourseDAO courseDAO;
    private final StudentDAO studentDAO;

    // Default constructor for production / existing callers
    public CourseServiceImpl() {
        this(new CourseDAOImpl(), new StudentDAOImpl());
    }

    // Constructor injection for testability
    public CourseServiceImpl(CourseDAO courseDAO, StudentDAO studentDAO) {
        this.courseDAO = courseDAO;
        this.studentDAO = studentDAO;
    }
    @Override
    public Course createCourse(String title, int credit) {
        Course course = new Course(title, credit);
        courseDAO.save(course);
        return course;
    }
    @Override
    public Course updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            return null;
        }
        course.setTitle(title);
        course.setCredit(credit);
        courseDAO.update(course);
        return courseDAO.findById(id);
    }
    @Override
    public boolean deleteCourse(int id) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            return false;
        }
        for (Student student : new ArrayList<>(course.getStudents())) {
            Student managedStudent = studentDAO.findById(student.getId());
            if (managedStudent != null) {
                managedStudent.removeCourse(course);
                studentDAO.update(managedStudent);
            }
        }
        courseDAO.delete(id);
        return true;
    }
    @Override
    public Course getCourseById(int id) {
        return courseDAO.findById(id);
    }
    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }
    @Override
    public List<Student> getStudentsOfCourse(int courseId) {
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(course.getStudents());
    }
    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findCoursesWithCreditGreaterThan(credit);
    }
    @Override
    public List<Object[]> countStudentsInEachCourse() {
        return courseDAO.countStudentsInEachCourse();
    }
}
