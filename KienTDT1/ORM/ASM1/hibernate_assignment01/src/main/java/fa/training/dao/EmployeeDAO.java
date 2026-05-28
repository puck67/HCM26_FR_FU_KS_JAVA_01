package fa.training.dao;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeDAO {

    // INSERT
    public void insertEmployee(Employee employee) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            session.persist(employee);

            tx.commit();

            System.out.println("Insert successfully!");

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }

    // GET BY ID
    public Employee getEmployeeById(int id) {

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            return session.get(Employee.class, id);

        }
    }

    // GET ALL
    public List<Employee> getAllEmployees() {

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            return session
                    .createQuery("FROM Employee", Employee.class)
                    .list();

        }
    }

    // UPDATE
    public void updateEmployee(Employee employee) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            session.merge(employee);

            tx.commit();

            System.out.println("Update successfully!");

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteEmployeeById(int id) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtils
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            Employee employee =
                    session.get(Employee.class, id);

            if (employee != null) {
                session.remove(employee);
            }

            tx.commit();

            System.out.println("Delete successfully!");

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }
}