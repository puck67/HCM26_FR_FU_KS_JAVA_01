package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    private void executeInTransaction(java.util.function.Consumer<Session> action) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    @Override
    public void save(Student student) {
        executeInTransaction(session -> session.persist(student));
    }

    @Override
    public void update(Student student) {
        executeInTransaction(session -> session.merge(student));
    }

    @Override
    public void delete(int studentId) {
        executeInTransaction(session -> {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                for (Course course : List.copyOf(student.getCourses())) {
                    student.removeCourse(course);
                }
                session.remove(student);
            }
        });
    }

    @Override
    public Student findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        }
    }

    @Override
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        }
    }

    @Override
    public List<Student> findOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Student s WHERE s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        }
    }

    @Override
    public List<Student> findWithCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses",
                            Student.class)
                    .list();
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
        }
    }

    @Override
    public List<Student> findByCourseId(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT s FROM Student s JOIN s.courses c WHERE c.id = :courseId",
                            Student.class)
                    .setParameter("courseId", courseId)
                    .list();
        }
    }
}