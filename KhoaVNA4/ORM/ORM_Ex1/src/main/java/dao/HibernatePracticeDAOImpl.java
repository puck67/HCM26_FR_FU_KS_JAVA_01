package dao;

import database.HibernateUtil;
import entities.Course;
import entities.Student;
import org.hibernate.Session;
import java.util.List;

public class HibernatePracticeDAOImpl implements HibernatePracticeDAO {

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Student s where s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Object[]> findStudentsAndCoursesHqlJoin() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select s.name, c.title from Student s join s.courses c", Object[].class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Student> findByNameNamedQuery(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            jakarta.persistence.criteria.CriteriaBuilder cb = session.getCriteriaBuilder();
            jakarta.persistence.criteria.CriteriaQuery<Course> cr = cb.createQuery(Course.class);
            jakarta.persistence.criteria.Root<Course> root = cr.from(Course.class);
            cr.select(root).where(cb.gt(root.get("credit"), credit));
            return session.createQuery(cr).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Object[]> countStudentsEnrolledInEachCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "select c.title, count(s.id) from Course c left join c.students s group by c.id, c.title", 
                Object[].class
            ).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
