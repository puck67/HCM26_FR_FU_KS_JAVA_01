package com.example.dao.impl;

import com.example.dao.CourseDAO;
import com.example.entity.Course;
import com.example.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CourseDAOImpl implements CourseDAO {
    private static final Logger logger = LoggerFactory.getLogger(CourseDAOImpl.class);

    @Override
    public void save(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving course", e);
            throw new RuntimeException("Failed to save course: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating course", e);
            throw new RuntimeException("Failed to update course: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Since Student is the owner of ManyToMany, we must first remove this course from all students.
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                // To safely remove without throwing foreign key violations, remove references in student_course join table.
                // We load students associated with this course and remove the course from their sets.
                for (var student : course.getStudents()) {
                    student.getCourses().remove(course);
                }
                course.getStudents().clear();
                session.remove(course);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting course", e);
            throw new RuntimeException("Failed to delete course: " + e.getMessage(), e);
        }
    }

    @Override
    public Course findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        } catch (Exception e) {
            logger.error("Error finding course by ID", e);
            throw new RuntimeException("Failed to find course: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Course", Course.class).list();
        } catch (Exception e) {
            logger.error("Error finding all courses", e);
            throw new RuntimeException("Failed to list courses: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Course> findWithCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Criteria API query
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);
            cq.select(root).where(cb.gt(root.get("credit"), credit));
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            logger.error("Error finding courses with credit greater than threshold", e);
            throw new RuntimeException("Failed to query courses by credit: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Object[]> getEnrollmentCounts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Aggregation query: count how many students are enrolled in each course
            String hql = "select c.title, count(s.id) from Course c left join c.students s group by c.id, c.title";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            return query.list();
        } catch (Exception e) {
            logger.error("Error getting course enrollment counts", e);
            throw new RuntimeException("Failed to get course enrollment counts: " + e.getMessage(), e);
        }
    }
}
