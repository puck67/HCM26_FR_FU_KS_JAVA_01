package fa.training.dao;

import fa.training.entities.Student;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class StudentDAO {

    public void save(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void update(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(student);
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
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.remove(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Student findById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Student> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Student", Student.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Task 5: HQL - Students older than given age
    public List<Student> findByAgeGreaterThan(int age) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Student s WHERE s.age > :age";
            Query<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("age", age);
            return query.list();
        }
    }

    // Task 5: HQL with Join - List students and courses
    public List<Object[]> findAllWithCourses() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT s.name, c.title FROM Student s JOIN s.courses c";
            return session.createQuery(hql, Object[].class).list();
        }
    }

    // Task 5: Named Query - Find by name
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Student> query = session.createNamedQuery("Student.findByName", Student.class);
            query.setParameter("name", name);
            return query.list();
        }
    }

    // BONUS: Pagination
    public List<Student> findAllPaginated(int page, int size) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Student", Student.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .list();
        }
    }

    // BONUS: Students not enrolled in any course
    public List<Student> findNotEnrolled() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student s WHERE s.courses IS EMPTY", Student.class).list();
        }
    }
}
