package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createCourse(String title, int credit) {
        Course course = new Course(title, credit);
        courseDAO.save(course);
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Optional.ofNullable(courseDAO.findById(id)).ifPresent(course -> {
            course.setTitle(title);
            course.setCredit(credit);
            courseDAO.update(course);
        });
    }

    @Override
    public void deleteCourse(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Optional.ofNullable(session.get(Course.class, id)).ifPresent(course -> {
                course.getStudents().forEach(student -> student.getCourses().remove(course));
                session.remove(course);
            });
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
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
            return Optional.ofNullable(session.get(Course.class, courseId))
                    .map(course -> {
                        course.getStudents().size();
                        return new HashSet<>(course.getStudents()); 
                    })
                    .orElseGet(HashSet::new);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    public List<Course> getCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findCoursesWithCreditGreaterThan(credit);
    }

    @Override
    public List<Object[]> getStudentCountPerCourse() {
        return courseDAO.countStudentsInEachCourse();
    }
}
