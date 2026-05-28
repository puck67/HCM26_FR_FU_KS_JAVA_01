package fa.training.dao;

import fa.training.entities.Student;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class StudentDAO {

    // CREATE
    public void save(Student student) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            session.persist(student);

            tx.commit();

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }

    // GET ALL
    public List<Student> getAll() {

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            return session
                    .createQuery(
                            "FROM Student",
                            Student.class
                    )
                    .list();
        }
    }

    public List<Student> findByName(String name) {

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            return session
                    .createNamedQuery(
                            "Student.findByName",
                            Student.class
                    )
                    .setParameter(
                            "name",
                            "%" + name + "%"
                    )
                    .list();
        }
    }

    // GET BY ID
    public Student getById(int id) {

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            return session.get(Student.class, id);
        }
    }

    // UPDATE
    public void update(Student student) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            session.merge(student);

            tx.commit();

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }

    // DELETE
    public void delete(Student student) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            session.remove(student);

            tx.commit();

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }
}