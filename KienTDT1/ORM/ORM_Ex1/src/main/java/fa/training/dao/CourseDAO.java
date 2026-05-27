package fa.training.dao;

import fa.training.entities.Course;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CourseDAO {

    // CREATE
    public void save(Course course) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            session.persist(course);

            tx.commit();

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }

    // GET ALL
    public List<Course> getAll() {

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            return session
                    .createQuery(
                            "FROM Course",
                            Course.class
                    )
                    .list();
        }
    }

    public Course getById(int id) {

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            return session.get(Course.class, id);
        }
    }

    // UPDATE
    public void update(Course course) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            session.merge(course);

            tx.commit();

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }

    // DELETE
    public void delete(Course course) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            session.remove(course);

            tx.commit();

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }
}