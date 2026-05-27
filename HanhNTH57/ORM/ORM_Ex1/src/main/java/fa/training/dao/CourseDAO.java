package fa.training.dao;

import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.utils.HibernateUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class CourseDAO {

    public void save(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<Course> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Course", Course.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Course findById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if (course != null) {
                // Ngắt kết nối với tất cả sinh viên trước khi xóa (xóa link trong bảng student_course)
                for (Student student : course.getStudents()) {
                    student.getCourses().remove(course);
                    session.merge(student);
                }
                session.remove(course);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Task 5: Criteria API - Courses with credit > given value
    public List<Course> findByCreditGreaterThan(int credit) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);
            cq.select(root).where(cb.gt(root.get("credit"), credit));
            return session.createQuery(cq).getResultList();
        }
    }

    // Task 5: Aggregation Query - Student count per course
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT c.title, COUNT(s) FROM Course c LEFT JOIN c.students s GROUP BY c.title";
            return session.createQuery(hql, Object[].class).list();
        }
    }
}
