package com.example.dao;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentDAO extends GenericDAO<Student> {

    public StudentDAO() {
        super(Student.class);
    }

    // Task 4: Enroll a student in a course 
    public void enrollStudent(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student != null && course != null) {
                student.addCourse(course);
                session.merge(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Task 4: Remove a student from a course
    public void unenrollStudent(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student != null && course != null) {
                student.removeCourse(course);
                session.merge(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Task 4: Display all courses of a given student
    public List<Course> getCoursesByStudentId(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.createQuery(
                    "SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.id = :id", Student.class)
                    .setParameter("id", studentId)
                    .uniqueResult();
            return student != null ? new ArrayList<>(student.getCourses()) : Collections.emptyList();
        }
    }

    // Task 5.1: HQL Query
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Student WHERE age > :age";
            return session.createQuery(hql, Student.class)
                    .setParameter("age", age)
                    .list();
        }
    }

    // Task 5.2: HQL with Join
    public void printStudentsAndCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hqlJoin = "SELECT s.name, c.title FROM Student s JOIN s.courses c";
            List<Object[]> results = session.createQuery(hqlJoin, Object[].class).list();
            for (Object[] row : results) {
                System.out.println("Student: " + row[0] + " - Course: " + row[1]);
            }
        }
    }

    // Task 5.3: Named Query
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        }
    }
}
