package fa.training.dao;

import fa.training.entities.Employee;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

// DAO handles CRUD operations for Employee
public class EmployeeDAO {

    // Insert a new Employee into the database
    public void insertEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
            System.out.println("Employee added successfully: " + employee);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Error adding Employee: " + e.getMessage());
        }
    }

    // Get Employee by ID
    public Employee getEmployeeById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employee employee = session.get(Employee.class, id);
            if (employee == null) {
                System.out.println("No Employee found with ID: " + id);
            }
            return employee;
        } catch (Exception e) {
            System.out.println("Error finding Employee: " + e.getMessage());
            return null;
        }
    }

    // Get all Employees from the database
    @SuppressWarnings("unchecked")
    public List<Employee> getAllEmployees() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Employee").list();
        } catch (Exception e) {
            System.out.println("Error retrieving Employee list: " + e.getMessage());
            return null;
        }
    }

    // Update Employee information by ID
    public void updateEmployeeById(int id, String newFirstName, String newLastName) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                employee.setFirstName(newFirstName);
                employee.setLastName(newLastName);
                session.update(employee);
                transaction.commit();
                System.out.println("Employee updated successfully: " + employee);
            } else {
                System.out.println("No Employee found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Error updating Employee: " + e.getMessage());
        }
    }

    // Delete Employee by ID
    public void deleteEmployeeById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
                transaction.commit();
                System.out.println("Employee with ID " + id + " deleted successfully.");
            } else {
                System.out.println("No Employee found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Error deleting Employee: " + e.getMessage());
        }
    }
}
