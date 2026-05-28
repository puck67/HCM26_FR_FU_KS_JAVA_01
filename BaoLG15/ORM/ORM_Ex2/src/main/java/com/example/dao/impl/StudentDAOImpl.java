package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StudentDAOImpl extends GenericDAOImpl<Student, Integer> implements StudentDAO {

    private static final Logger logger = LoggerFactory.getLogger(StudentDAOImpl.class);

    public StudentDAOImpl() {
        super(Student.class);
    }

    @Override
    public List<Student> findOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT s FROM Student s WHERE s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding students older than age: {}", age, e);
            throw new RuntimeException("Failed to find students older than age: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Object[]> findStudentsWithCourseTitles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT s.name, c.title FROM Student s JOIN s.courses c", Object[].class).list();
        } catch (Exception e) {
            logger.error("Error finding students with course titles", e);
            throw new RuntimeException("Failed to find students with course titles: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding student by name: {}", name, e);
            throw new RuntimeException("Failed to find student by name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findStudentsByCourseId(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId", Student.class)
                    .setParameter("courseId", courseId)
                    .list();
        } catch (Exception e) {
            logger.error("Error finding students by course ID: {}", courseId, e);
            throw new RuntimeException("Failed to find students by course: " + e.getMessage(), e);
        }
    }

    @Override
    public Student findByIdWithCourses(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.id = :id", Student.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding student with courses by ID: {}", id, e);
            throw new RuntimeException("Failed to find student with courses: " + e.getMessage(), e);
        }
    }
}
