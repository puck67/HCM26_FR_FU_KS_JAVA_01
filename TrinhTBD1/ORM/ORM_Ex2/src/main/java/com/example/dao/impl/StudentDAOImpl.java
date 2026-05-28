package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StudentDAOImpl implements StudentDAO {
    private static final Logger logger = LoggerFactory.getLogger(StudentDAOImpl.class);

    @Override
    public void save(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving student", e);
            throw new RuntimeException("Failed to save student: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating student", e);
            throw new RuntimeException("Failed to update student: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int studentId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                // Clear course associations to prevent foreign key errors in the student_course table
                student.getCourses().clear();
                session.remove(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting student", e);
            throw new RuntimeException("Failed to delete student: " + e.getMessage(), e);
        }
    }

    @Override
    public Student findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        } catch (Exception e) {
            logger.error("Error finding student by ID", e);
            throw new RuntimeException("Failed to find student: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Student", Student.class).list();
        } catch (Exception e) {
            logger.error("Error finding all students", e);
            throw new RuntimeException("Failed to list students: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("from Student s where s.age > :age", Student.class);
            query.setParameter("age", age);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding students older than age", e);
            throw new RuntimeException("Failed to query students by age: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createNamedQuery("Student.findByName", Student.class);
            query.setParameter("name", name);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding students by name", e);
            throw new RuntimeException("Failed to query students by name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findAllWithCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL Join query: distinct fetch courses to load them eager in one query
            return session.createQuery("select distinct s from Student s left join fetch s.courses", Student.class).list();
        } catch (Exception e) {
            logger.error("Error finding all students with courses", e);
            throw new RuntimeException("Failed to query students with courses: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findStudentsByCourseId(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Parameterized query: find students enrolled in a specific course
            Query<Student> query = session.createQuery(
                "select s from Student s join s.courses c where c.id = :courseId", Student.class);
            query.setParameter("courseId", courseId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding students by course ID", e);
            throw new RuntimeException("Failed to query students of course: " + e.getMessage(), e);
        }
    }
}
