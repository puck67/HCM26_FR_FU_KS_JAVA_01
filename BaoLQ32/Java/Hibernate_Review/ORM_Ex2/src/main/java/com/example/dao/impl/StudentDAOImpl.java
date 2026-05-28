package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int studentId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                // Remove this student from all courses first to clean up association tables
                // (though JPA cascade/join table usually handles this, we do it cleanly)
                student.getCourses().clear();
                session.remove(student);
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
    public Student findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Eagerly load courses inside this session if needed, but since we close session,
            // we can initialize the courses collection to avoid LazyInitializationException.
            Student student = session.get(Student.class, id);
            if (student != null) {
                student.getCourses().size(); // Force initialization
            }
            return student;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Student> students = session.createQuery("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses", Student.class).list();
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Student> findOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Using Named Query defined in Student entity
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Object[]> findStudentsWithCourseTitles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL Join Query: returns Student and Course title
            return session.createQuery("SELECT s, c.title FROM Student s LEFT JOIN s.courses c", Object[].class)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Student> findStudentsByCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Parameterized Query: find students enrolled in a specific course
            return session.createQuery("SELECT DISTINCT s FROM Student s JOIN s.courses c WHERE c.id = :courseId", Student.class)
                    .setParameter("courseId", courseId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
