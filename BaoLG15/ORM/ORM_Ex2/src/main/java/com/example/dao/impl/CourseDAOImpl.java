package com.example.dao.impl;

import com.example.dao.CourseDAO;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CourseDAOImpl extends GenericDAOImpl<Course, Integer> implements CourseDAO {

    private static final Logger logger = LoggerFactory.getLogger(CourseDAOImpl.class);

    public CourseDAOImpl() {
        super(Course.class);
    }

    @Override
    public void delete(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.createQuery(
                            "SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.id = :id", Course.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (course != null) {
                for (Student student : course.getStudents()) {
                    student.getCourses().remove(course);
                }
                session.remove(course);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting course by ID: {}", id, e);
            throw new RuntimeException("Failed to delete course: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);
            cq.select(root).where(cb.gt(root.get("credit"), credit));
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            logger.error("Error finding courses with credit greater than: {}", credit, e);
            throw new RuntimeException("Failed to find courses by credit: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c.title, COUNT(s.id) FROM Course c LEFT JOIN c.students s GROUP BY c.id, c.title", Object[].class).list();
        } catch (Exception e) {
            logger.error("Error counting students per course", e);
            throw new RuntimeException("Failed to count students per course: " + e.getMessage(), e);
        }
    }

    @Override
    public Course findByIdWithStudents(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.id = :id", Course.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding course with students by ID: {}", id, e);
            throw new RuntimeException("Failed to find course with students: " + e.getMessage(), e);
        }
    }
}
