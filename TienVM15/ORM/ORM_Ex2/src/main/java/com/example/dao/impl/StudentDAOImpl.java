package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * StudentDAOImpl extends GenericDAOImpl and provides Student-specific DAO methods.
 */
public class StudentDAOImpl extends GenericDAOImpl<Student, Integer> implements StudentDAO {

    public StudentDAOImpl() {
        super(Student.class);
    }

    /** Initialize lazy-loaded courses collection within active session */
    @Override
    protected void initLazyCollections(Student student) {
        if (student != null && student.getCourses() != null) {
            student.getCourses().size(); // trigger proxy initialization
        }
    }

    /** Dissociate student from all courses before deletion to avoid FK constraint */
    @Override
    protected void beforeDelete(Student student) {
        if (student != null && student.getCourses() != null) {
            student.getCourses().forEach(course -> course.getStudents().remove(student));
            student.getCourses().clear();
        }
    }

    @Override
    public List<Student> findAllPaginated(int offset, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            List<Student> students = query.list();
            for (Student s : students) {
                s.getCourses().size();
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Student> findOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery(
                "FROM Student s WHERE s.age > :age", Student.class);
            query.setParameter("age", age);
            List<Student> students = query.list();
            for (Student s : students) {
                s.getCourses().size();
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Object[]> findStudentsAndCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL Join: returns [Student, Course] pairs for each enrollment
            Query<Object[]> query = session.createQuery(
                "SELECT s, c FROM Student s JOIN s.courses c", Object[].class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Execute Named Query defined on Student entity
            Query<Student> query = session.createNamedQuery("Student.findByName", Student.class);
            query.setParameter("name", "%" + name + "%");
            List<Student> students = query.list();
            for (Student s : students) {
                s.getCourses().size();
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Student> findUnenrolledStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery(
                "FROM Student s WHERE s.courses IS EMPTY", Student.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
