package fa.training.dao;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Collections;
import java.util.List;

public class EmployeeDao {

    // 1. Insert/Save
    public boolean insertEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 2. Get By ID
    public Employee getEmployeeById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 3. Get All
    public List<Employee> getAllEmployees() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Employee", Employee.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 4. Update By ID
    public boolean updateEmployeeById(int id, String newFirstName, String newLastName) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                employee.setFirstName(newFirstName);
                employee.setLastName(newLastName);
                session.update(employee);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 5. Delete By ID
    public boolean deleteEmployeeById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
