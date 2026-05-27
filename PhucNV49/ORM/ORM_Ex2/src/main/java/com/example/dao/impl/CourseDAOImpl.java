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
            throw new RuntimeException("Error saving course: " + e.getMessage(), e);
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
            throw new RuntimeException("Error updating course: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                // Since Course is the inverse side (mappedBy), we must remove it from each student's courses list
                // so Hibernate updates the student_course join table.
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
            throw new RuntimeException("Error deleting course: " + e.getMessage(), e);
        }
    }

    @Override
    public Course findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, id);
            if (course != null) {
                org.hibernate.Hibernate.initialize(course.getStudents());
            }
            return course;
        } catch (Exception e) {
            throw new RuntimeException("Error finding course by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Course", Course.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all courses: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Course> findByCreditGreaterThanCriteria(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);
            
            cq.select(root).where(cb.greaterThan(root.get("credit"), credit));
            
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error executing Criteria query: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Object[]> getStudentCountPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Group by course title and count students
            return session.createQuery(
                    "select c.title, count(s.id) from Course c left join c.students s group by c.id, c.title", 
                    Object[].class
            ).list();
        } catch (Exception e) {
            throw new RuntimeException("Error executing aggregation query: " + e.getMessage(), e);
        }
    }
}
