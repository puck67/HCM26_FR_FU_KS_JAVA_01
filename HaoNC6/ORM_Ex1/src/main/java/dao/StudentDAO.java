package dao;

import entity.Course;
import entity.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Set;

/**
 * StudentDAO - CRUD and query operations for Student entity.
 */
public class StudentDAO {

    // ========== CRUD ==========

    /**
     * Create a new student
     */
    public void create(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Update student information
     */
    public void update(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Delete a student by ID
     */
    public void delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                for (Course c : student.getCourses()) {
                    c.getStudents().remove(student);
                }
                student.getCourses().clear();
                session.remove(student);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Get a student by ID
     */
    public Student getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        }
    }

    /**
     * Get all students
     */
    public List<Student> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        }
    }


    /**
     * Enroll a student in a course
     */
    public void enroll(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.enroll(course);
                session.merge(student);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Remove a student from a course
     */
    public void unenroll(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.unenroll(course);
                session.merge(student);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Get all courses of a student
     */
    public Set<Course> getCoursesOfStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                // Initialize lazy collection
                org.hibernate.Hibernate.initialize(student.getCourses());
                return student.getCourses();
            }
            return new java.util.HashSet<>();
        }
    }

    // ========== Task 5: Queries ==========

    /**
     * HQL: Find all students older than a given age.
     */
    public List<Student> findOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Student s WHERE s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        }
    }

    /**
     * HQL with Join: List students and courses they are enrolled in.
     */
    public List<Object[]> findStudentsWithCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT s.name, c.title FROM Student s JOIN s.courses c", Object[].class)
                    .list();
        }
    }

    /**
     * Named Query: Find students by name.
     */
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        }
    }

    /**
     * Aggregation: Count how many students are enrolled in each course.
     */
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c.title, COUNT(s) FROM Course c JOIN c.students s GROUP BY c.title",
                            Object[].class)
                    .list();
        }
    }

    /**
     * BONUS: Find students not enrolled in any course.
     */
    public List<Student> findStudentsNotEnrolled() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Student s WHERE s.courses IS EMPTY", Student.class)
                    .list();
        }
    }

    /**
     * BONUS: Paginated list of all students.
     */
    public List<Student> getAllPaginated(int pageNumber, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        }
    }
}