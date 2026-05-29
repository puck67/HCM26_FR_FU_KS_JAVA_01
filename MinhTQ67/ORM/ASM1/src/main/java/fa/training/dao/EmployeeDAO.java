package fa.training.dao;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * DAO (Data Access Object) class for Employee entity.
 * Provides at least 5 CRUD methods as required by the assignment.
 */
public class EmployeeDAO {

    /**
     * Find an Employee by its ID.
     *
     * @param id the employee ID
     * @return the Employee object, or null if not found
     */
    public Employee getEmployeeByID(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error in getEmployeeByID: " + e.getMessage(), e);
        }
    }

    /**
     * Get all Employees from the database.
     *
     * @return list of all Employee objects
     */
    public List<Employee> getAllEmployees() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL query - uses class name, not table name
            return session.createQuery("FROM Employee", Employee.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error in getAllEmployees: " + e.getMessage(), e);
        }
    }

    /**
     * Insert a new Employee into the database.
     *
     * @param employee the Employee object to insert
     * @return true if successful
     */
    public boolean insertEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error in insertEmployee: " + e.getMessage(), e);
        }
    }

    /**
     * Update an existing Employee's first name and last name by ID.
     *
     * @param id        the ID of the employee to update
     * @param firstName new first name
     * @param lastName  new last name
     * @return true if successful, false if not found
     */
    public boolean updateEmployeeByID(int id, String firstName, String lastName) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                employee.setFirstName(firstName);
                employee.setLastName(lastName);
                session.update(employee);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error in updateEmployeeByID: " + e.getMessage(), e);
        }
    }

    /**
     * Delete an Employee by ID.
     *
     * @param id the ID of the employee to delete
     * @return true if successful, false if not found
     */
    public boolean deleteEmployeeById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error in deleteEmployeeById: " + e.getMessage(), e);
        }
    }
    /**
     * Check if an Employee with the exact first name and last name already exists.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @return true if exists, false otherwise
     */
    public boolean existsByFullName(String firstName, String lastName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "SELECT COUNT(e) FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName", Long.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error in existsByFullName: " + e.getMessage(), e);
        }
    }
}
