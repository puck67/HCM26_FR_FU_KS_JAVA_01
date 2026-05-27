package fa.training.daoImpl;

import fa.training.config.HibernateUtil;
import fa.training.dao.QueryDAO;
import fa.training.entities.Course;
import fa.training.entities.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class QueryImpl implements QueryDAO {

    @Override
    public List<Student> findStudentsPaged(int pageNumber, int pageSize) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<Student> students = session.createQuery(
                            "from Student s order by s.id",
                            Student.class)
                    .setFirstResult(Math.max(pageNumber - 1, 0) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
            transaction.commit();
            return students;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<Student> students = session.createQuery(
                            "from Student s where s.age > :age order by s.age, s.name",
                            Student.class)
                    .setParameter("age", age)
                    .list();
            transaction.commit();
            return students;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Object[]> listStudentsAndCourses() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<Object[]> rows = session.createQuery(
                            "select s.name, c.title from Student s join s.courses c order by s.name, c.title",
                            Object[].class)
                    .list();
            transaction.commit();
            return rows;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<Student> students = session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
            transaction.commit();
            return students;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
            Root<Course> root = criteriaQuery.from(Course.class);
            criteriaQuery.select(root)
                    .where(criteriaBuilder.greaterThan(root.get("credit"), credit))
                    .orderBy(criteriaBuilder.asc(root.get("title")));
            List<Course> courses = session.createQuery(criteriaQuery).getResultList();
            transaction.commit();
            return courses;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<Object[]> rows = session.createQuery(
                            "select c.title, count(s.id) from Course c left join c.students s group by c.id, c.title order by c.title",
                            Object[].class)
                    .list();
            transaction.commit();
            return rows;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    @Override
    public List<Student> findStudentsNotEnrolledInAnyCourse() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<Student> students = session.createQuery(
                            "select s from Student s left join s.courses c where c is null order by s.id",
                            Student.class)
                    .list();
            transaction.commit();
            return students;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(ex);
        }
    }
}

