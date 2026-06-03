package fa.training.dao;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * DAO (Data Access Object) class for Employee entity.
 * Provides CRUD operations using Hibernate Session.
 */
public class EmployeeDAO {

    private SessionFactory sessionFactory;

    public EmployeeDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    // ===== 1. INSERT =====
    /**
     * Saves a new Employee record to the database.
     * @param employee the Employee object to insert
     * @return the generated ID of the new employee
     */
    public int insertEmployee(Employee employee) {
        Transaction transaction = null;
        int generatedId = 0;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            generatedId = (int) session.save(employee);
            transaction.commit();
            System.out.println("Inserted Employee with ID: " + generatedId);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error inserting Employee: " + e.getMessage());
            e.printStackTrace();
        }
        return generatedId;
    }

    // ===== 2. GET BY ID =====
    /**
     * Retrieves an Employee by their ID.
     * @param id the ID to search
     * @return the Employee object, or null if not found
     */
    public Employee getEmployeeByID(int id) {
        Employee employee = null;
        try (Session session = sessionFactory.openSession()) {
            employee = session.get(Employee.class, id);
            if (employee != null) {
                System.out.println("Found: " + employee);
            } else {
                System.out.println("No Employee found with ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error getting Employee by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return employee;
    }

    // ===== 3. GET ALL =====
    /**
     * Retrieves all Employee records from the database.
     * @return list of all employees
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Employee> query = session.createQuery("FROM Employee", Employee.class);
            employees = query.getResultList();
            System.out.println("Total employees found: " + employees.size());
        } catch (Exception e) {
            System.err.println("Error getting all Employees: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }

    // ===== 4. UPDATE BY ID =====
    /**
     * Updates an existing Employee's information by ID.
     * @param id        the ID of the employee to update
     * @param firstName new first name
     * @param lastName  new last name
     * @return true if updated successfully, false otherwise
     */
    public boolean updateEmployeeByID(int id, String firstName, String lastName) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                employee.setFirstName(firstName);
                employee.setLastName(lastName);
                session.update(employee);
                transaction.commit();
                System.out.println("Updated Employee ID " + id + ": " + employee);
                return true;
            } else {
                System.out.println("Employee with ID " + id + " not found. Nothing updated.");
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error updating Employee: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ===== 5. DELETE BY ID =====
    /**
     * Deletes an Employee record by ID.
     * @param id the ID of the employee to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteEmployeeById(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
                transaction.commit();
                System.out.println("Deleted Employee with ID: " + id);
                return true;
            } else {
                System.out.println("Employee with ID " + id + " not found. Nothing deleted.");
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error deleting Employee: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
