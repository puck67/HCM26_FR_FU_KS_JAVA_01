package fa.training.service.impl;

import fa.training.dao.CourseDAO;
import fa.training.dao.QueryDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.dao.impl.QueryDAOImpl;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.service.CourseService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();
    private final QueryDAO queryDAO = new QueryDAOImpl();

    @Override
    public void createCourse(String title, int credit) {
        Course course = new Course(title, credit);
        courseDAO.add(course);
        System.out.println("Course created successfully.");
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course != null) {
            course.setTitle(title);
            course.setCredit(credit);
            courseDAO.update(course);
            System.out.println("Course updated successfully.");
        } else {
            System.out.println("Course not found.");
        }
    }

    @Override
    public void deleteCourse(int id) {
        if (courseDAO.findById(id) != null) {
            courseDAO.delete(id);
            System.out.println("Course deleted successfully.");
        } else {
            System.out.println("Course not found.");
        }
    }

    @Override
    public Course getCourseById(int id) {
        return courseDAO.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.getAll();
    }

    @Override
    public Set<Student> getStudentsOfCourse(int courseId) {
        Course course = courseDAO.findById(courseId);
        if (course != null) {
            return course.getStudents();
        }
        return Collections.emptySet();
    }

    @Override
    public List<Course> getCoursesWithCreditGreaterThan(int credit) {
        return queryDAO.findCoursesByCreditGreaterThan(credit);
    }

    @Override
    public List<Object[]> getStudentCountPerCourse() {
        return queryDAO.countStudentsPerCourse();
    }
}
