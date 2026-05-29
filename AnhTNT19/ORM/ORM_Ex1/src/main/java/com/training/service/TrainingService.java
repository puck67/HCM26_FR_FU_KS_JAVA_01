package com.training.service;

import com.training.entity.Course;
import com.training.entity.Student;
import com.training.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TrainingService {

    public void saveStudent(Student student) {
        executeInTransaction(session -> session.persist(student));
    }

    public void updateStudent(Student student) {
        executeInTransaction(session -> session.merge(student));
    }

    public void deleteStudent(int id) {
        executeInTransaction(session -> {
            Student student = session.get(Student.class, id);
            if (student != null) {
                for (Course course : java.util.List.copyOf(student.getCourses())) {
                    student.removeCourse(course);
                }
                session.remove(student);
            }
        });
    }

    public Student getStudentById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        }
    }

    public List<Student> getAllStudents(Integer offset, Integer limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            if (offset != null) query.setFirstResult(offset);
            if (limit != null) query.setMaxResults(limit);
            return query.list();
        }
    }

    public void saveCourse(Course course) {
        executeInTransaction(session -> session.persist(course));
    }

    public void updateCourse(Course course) {
        executeInTransaction(session -> session.merge(course));
    }

    public void deleteCourse(int id) {
        executeInTransaction(session -> {
            Course course = session.get(Course.class, id);
            if (course != null) {
                for (Student student : java.util.List.copyOf(course.getStudents())) {
                    student.removeCourse(course);
                }
                session.remove(course);
            }
        });
    }

    public List<Course> getAllCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Course", Course.class).list();
        }
    }

    public void enrollStudentInCourse(int studentId, int courseId) {
        executeInTransaction(session -> {
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.addCourse(course);
            }
        });
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        executeInTransaction(session -> {
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.removeCourse(course);
            }
        });
    }

    public List<Course> getCoursesByStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                student.getCourses().size(); // Force initialization of lazy collection
                return List.copyOf(student.getCourses());
            }
            return List.of();
        }
    }

    public List<Student> getStudentsByCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                course.getStudents().size(); // Force initialization of lazy collection
                return List.copyOf(course.getStudents());
            }
            return List.of();
        }
    }

    // ==========================================
    // TASK 5: ADVANCED HIBERNATE QUERIES
    // ==========================================

    // 1. HQL: Find all students older than a given age
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student s WHERE s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        }
    }

    // 2. HQL with Join: Fetch students along with their courses eagerly
    public List<Student> getStudentsWithCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses", Student.class)
                    .list();
        }
    }

    // 3. Named Query: Find student by name
    public List<Student> findStudentsByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        }
    }

    // 4. Criteria API: Find courses with credits greater than value
    public List<Course> findCoursesWithCreditGreaterThan(int creditValue) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cr = cb.createQuery(Course.class);
            Root<Course> root = cr.from(Course.class);

            cr.select(root).where(cb.gt(root.get("credit"), creditValue));

            return session.createQuery(cr).getResultList();
        }
    }

    // 5. Aggregation Query: Count how many students are enrolled in each course
    public List<Object[]> getStudentCountPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c.title, COUNT(s.id) FROM Course c LEFT JOIN c.students s GROUP BY c.title",
                            Object[].class)
                    .list();
        }
    }

    // BONUS 2: Find students who are not enrolled in any course
    public List<Student> findUnenrolledStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student s WHERE s.courses IS EMPTY", Student.class)
                    .list();
        }
    }

    // ==========================================
    // TRANSACTION MANAGEMENT HELPER
    // ==========================================
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
}