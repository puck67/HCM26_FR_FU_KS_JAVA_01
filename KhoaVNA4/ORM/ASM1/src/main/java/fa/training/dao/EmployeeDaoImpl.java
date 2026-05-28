package fa.training.dao;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Collections;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

    @Override
    public void insertEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
            System.out.println("Inserted completely. Generated ID: " + employee.getId());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Insertion failed: " + e.getMessage());
        }
    }

    @Override
    public Employee getEmployeeById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        } catch (Exception e) {
            System.err.println("Error fetching employee: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Employee> getAllEmployee() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Employee", Employee.class).list();
        } catch (Exception e) {
            System.err.println("Error fetching list: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void updateEmployeeById(int id, String newFirstName, String newLastName) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee emp = session.get(Employee.class, id);
            if (emp != null) {
                emp.setFirstName(newFirstName);
                emp.setLastName(newLastName);
                transaction.commit();
                System.out.println("Updated employee ID " + id + " successfully.");
            } else {
                System.out.println("Employee with ID " + id + " does not exist.");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Update failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteEmployeeById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee emp = session.get(Employee.class, id);
            if (emp != null) {
                session.remove(emp);
                transaction.commit();
                System.out.println("Deleted employee ID " + id + " successfully.");
            } else {
                System.out.println("Employee with ID " + id + " does not exist.");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Delete failed: " + e.getMessage());
        }
    }
}
