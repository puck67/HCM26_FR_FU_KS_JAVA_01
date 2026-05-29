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
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    // Khoi tao DAO mon hoc
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createCourse(String title, int credit) {
        // Kiem tra du lieu dau vao
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Tieu de mon hoc khong duoc de trong!");
        }
        if (credit <= 0) {
            throw new IllegalArgumentException("So tin chi phai lon hon 0!");
        }

        Course course = new Course(title, credit);
        courseDAO.save(course);
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID khong hop le!");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Tieu de mon hoc khong duoc de trong!");
        }
        if (credit <= 0) {
            throw new IllegalArgumentException("So tin chi phai lon hon 0!");
        }

        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new IllegalArgumentException("Khong tim thay mon hoc co ID = " + id);
        }

        course.setTitle(title);
        course.setCredit(credit);
        courseDAO.update(course);
    }

    @Override
    public void deleteCourse(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID khong hop le!");
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Course course = session.get(Course.class, id);

            if (course == null) {
                throw new IllegalArgumentException("Khong tim thay mon hoc co ID = " + id);
            }

            // Go bo moi quan he ManyToMany truoc khi xoa khoa hoc
            for (Student student : course.getStudents()) {
                student.getCourses().remove(course);
            }
            course.getStudents().clear();

            session.remove(course);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            }
            e.printStackTrace();
            throw new RuntimeException("Loi khi xoa mon hoc: " + e.getMessage());
        }
    }

    @Override
    public Course getCourseById(int id) {
        if (id <= 0) {
            return null;
        }
        return courseDAO.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public Set<Student> getStudentsOfCourse(int courseId) {
        if (courseId <= 0) {
            return new HashSet<>();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                // Load truoc lazy collection truoc khi close session
                course.getStudents().size();
                return new HashSet<>(course.getStudents());
            }
            return new HashSet<>();
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
