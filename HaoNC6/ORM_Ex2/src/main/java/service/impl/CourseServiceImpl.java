package service.impl;

import dao.CourseDAO;
import dao.impl.CourseDAOImpl;
import entity.Course;
import entity.Student;
import service.CourseService;
import util.HibernateUtil;
import org.hibernate.Session;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createCourse(String title, int credit) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty.");
        }
        if (credit <= 0) {
            throw new IllegalArgumentException("Course credit must be positive.");
        }
        courseDAO.save(new Course(title, credit));
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + id + " not found.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty.");
        }
        if (credit <= 0) {
            throw new IllegalArgumentException("Course credit must be positive.");
        }
        course.setTitle(title);
        course.setCredit(credit);
        courseDAO.update(course);
    }

    @Override
    public void deleteCourse(int id) {
        if (courseDAO.findById(id) == null) {
            throw new IllegalArgumentException("Course with ID " + id + " not found.");
        }
        courseDAO.delete(id);
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
    public Set<Student> getStudentsOfCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                // Initialize Lazy Collection
                org.hibernate.Hibernate.initialize(course.getStudents());
                return course.getStudents();
            }
            return new HashSet<>();
        }
    }

    // ========== Queries ==========

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findByMinCredit(credit);
    }
}
