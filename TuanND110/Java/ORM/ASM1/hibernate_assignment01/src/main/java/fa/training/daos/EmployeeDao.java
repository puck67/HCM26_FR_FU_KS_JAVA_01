package fa.training.daos;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Objects;

/**
 * EmployeeDao - Data Access Object for Employee entity
 */
public class EmployeeDao {

    /**
     * Insert a new employee
     */
    public Integer insertEmployee(Employee employee) {
        Objects.requireNonNull(employee, "employee must not be null");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer employeeId = null;
        try {
            transaction = session.beginTransaction();
            employeeId = (Integer) session.save(employee);
            transaction.commit();
            System.out.println("Employee inserted successfully with ID: " + employeeId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error inserting employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeId;
    }

    /**
     * Get employee by ID
     */
    public Employee getEmployeeById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Employee employee = null;
        try {
            employee = session.get(Employee.class, id);
        } catch (Exception e) {
            System.err.println("Error retrieving employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employee;
    }

    /**
     * Get all employees
     */
    public List<Employee> getAllEmployees() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Employee> employees = null;
        try {
            Query<Employee> query = session.createQuery("FROM Employee", Employee.class);
            employees = query.list();
        } catch (Exception e) {
            System.err.println("Error retrieving all employees: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employees;
    }

    /**
     * Update employee by ID
     */
    public void updateEmployeeById(Integer id, Employee employeeDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(employeeDetails, "employeeDetails must not be null");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                employee.setFirstName(employeeDetails.getFirstName());
                employee.setLastName(employeeDetails.getLastName());
                session.update(employee);
                transaction.commit();
                System.out.println("Employee updated successfully with ID: " + id);
            } else {
                System.out.println("Employee not found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Delete employee by ID
     */
    public void deleteEmployeeById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
                transaction.commit();
                System.out.println("Employee deleted successfully with ID: " + id);
            } else {
                System.out.println("Employee not found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
