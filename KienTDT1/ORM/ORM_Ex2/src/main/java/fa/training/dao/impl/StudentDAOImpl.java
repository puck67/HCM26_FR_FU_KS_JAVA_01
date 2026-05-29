package fa.training.dao.impl;

import fa.training.dao.StudentDAO;
import fa.training.entity.Student;
import fa.training.util.HibernateUtil;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    public void save(Student s) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(s);
        tx.commit();
        session.close();
    }



    public void update(Student s) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.merge(s);
        tx.commit();
        session.close();
    }

    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Student s = session.get(Student.class, id);
        if (s != null) session.remove(s);

        tx.commit();
        session.close();
    }
    public Student findById(int id) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Student s = session.get(Student.class, id);

        // initialize lazy collection
        Hibernate.initialize(s.getCourses());

        session.close();

        return s;
    }

    public List<Student> findAll() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Student> list =
                session.createQuery("FROM Student", Student.class).list();

        session.close();

        return list;
    }

    // HQL

    public List<Student> findStudentsOlderThan(int age) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Student> list = session.createQuery(
                        "FROM Student s WHERE s.age > :age",
                        Student.class
                )
                .setParameter("age", age)
                .list();

        session.close();

        return list;
    }

    // Named Query

    public List<Student> findByName(String name) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Student> list = session
                .createNamedQuery("Student.findByName", Student.class)
                .setParameter("name", name)
                .list();

        session.close();

        return list;
    }

    // HQL JOIN

    public List<Object[]> listStudentsAndCourses() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Object[]> list = session.createQuery(
                """
                SELECT s.name, c.title
                FROM Student s
                JOIN s.courses c
                """,
                Object[].class
        ).list();

        session.close();

        return list;
    }

    // Parameterized Query

    public List<Student> findStudentsByCourse(int courseId) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Student> list = session.createQuery(
                        """
                        SELECT s
                        FROM Student s
                        JOIN s.courses c
                        WHERE c.id = :courseId
                        """,
                        Student.class
                )
                .setParameter("courseId", courseId)
                .list();

        session.close();

        return list;
    }
}