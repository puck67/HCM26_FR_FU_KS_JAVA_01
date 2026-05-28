package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StudentDAOImpl implements StudentDAO {

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
            throw new RuntimeException("Error saving student: " + e.getMessage(), e);
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
            throw new RuntimeException("Error updating student: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int studentId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                // Clean up many-to-many relationship rows in the join table
                student.getCourses().clear();
                session.remove(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting student: " + e.getMessage(), e);
        }
    }

    @Override
    public Student findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, id);
            if (student != null) {
                org.hibernate.Hibernate.initialize(student.getCourses());
            }
            return student;
        } catch (Exception e) {
            throw new RuntimeException("Error finding student by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Student", Student.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all students: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findByAgeGreaterThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("from Student s where s.age > :age", Student.class);
            query.setParameter("age", age);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding students by age: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findByNameNamedQuery(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createNamedQuery("Student.findByName", Student.class);
            query.setParameter("name", name);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding students by name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Object[]> findAllStudentsWithCourseTitles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select s, c.title from Student s join s.courses c", Object[].class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error getting students with course titles: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findStudentsByCourseId(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery(
                    "select s from Student s join s.courses c where c.id = :courseId", Student.class);
            query.setParameter("courseId", courseId);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding students by course ID: " + e.getMessage(), e);
        }
    }
}
