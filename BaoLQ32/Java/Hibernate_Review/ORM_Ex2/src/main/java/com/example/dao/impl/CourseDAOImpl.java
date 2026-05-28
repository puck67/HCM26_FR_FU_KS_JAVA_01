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

import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {

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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                // Since Course is the target (mappedBy) side, we must remove it from
                // all associated Students' collections to clean up the student_course table entries.
                for (Student student : new ArrayList<>(course.getStudents())) {
                    student.removeCourse(course);
                    session.merge(student);
                }
                session.remove(course);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Course findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, id);
            if (course != null) {
                course.getStudents().size(); // Force initialization inside session
            }
            return course;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students", Course.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Course> findWithCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Criteria API query
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);
            cq.select(root).where(cb.greaterThan(root.get("credit"), credit));
            return session.createQuery(cq).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Aggregation query: count how many students are enrolled in each course
            return session.createQuery(
                    "SELECT c, COUNT(s) FROM Course c LEFT JOIN c.students s GROUP BY c.id, c.title, c.credit",
                    Object[].class
            ).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
