package service;

import entity.Course;
import entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ultil.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class EnrollmentService {

    // ─── Task 4: Enrollment Logic ───────────────────────────────────────────

    /** Enroll a student in a course */
    public void enroll(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);
            if (course == null) throw new IllegalArgumentException("Course not found: " + courseId);

            student.enroll(course);
            session.merge(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /** Remove a student from a course */
    public void unenroll(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student == null || course == null) return;

            student.unenroll(course);
            session.merge(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /** Get all courses a student is enrolled in */
    public List<Course> getCoursesOfStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM Student s JOIN s.courses c WHERE s.id = :sid";
            return session.createQuery(hql, Course.class)
                    .setParameter("sid", studentId)
                    .list();
        }
    }

    /** Get all students enrolled in a course */
    public List<Student> getStudentsOfCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM Course c JOIN c.students s WHERE c.id = :cid";
            return session.createQuery(hql, Student.class)
                    .setParameter("cid", courseId)
                    .list();
        }
    }

    // ─── Task 5: Query Practice ─────────────────────────────────────────────

    /**
     * HQL Query: Find all students older than a given age.
     */
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Student s WHERE s.age > :age ORDER BY s.age";
            return session.createQuery(hql, Student.class)
                    .setParameter("age", age)
                    .list();
        }
    }

    /**
     * HQL with JOIN: List students and the courses they are enrolled in.
     * Returns Object[] { Student, Course } pairs.
     */
    public List<Object[]> listStudentsWithCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s, c FROM Student s JOIN s.courses c ORDER BY s.name";
            return session.createQuery(hql, Object[].class).list();
        }
    }

    /**
     * Named Query: Find students by exact name.
     */
    public List<Student> findStudentsByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        }
    }

    /**
     * Criteria API: Find courses with credit greater than given value.
     */
    public List<Course> findCoursesByMinCredit(int minCredit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);

            cq.select(root)
                    .where(cb.greaterThan(root.get("credit"), minCredit))
                    .orderBy(cb.desc(root.get("credit")));

            return session.createQuery(cq).list();
        }
    }

    /**
     * Aggregation Query: Count how many students are enrolled in each course.
     * Returns Object[] { Course title (String), count (Long) }
     */
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c.title, COUNT(s) " +
                    "FROM Course c LEFT JOIN c.students s " +
                    "GROUP BY c.title " +
                    "ORDER BY COUNT(s) DESC";
            return session.createQuery(hql, Object[].class).list();
        }
    }

    /**
     * Bonus: Find students NOT enrolled in any course.
     */
    public List<Student> findStudentsWithNoCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Student s WHERE s.courses IS EMPTY";
            return session.createQuery(hql, Student.class).list();
        }
    }
}
