package fa.training.dao;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * DAO for Employee entity.
 *
 * All methods use try-with-resources on Session to guarantee
 * resource release and prevent connection leaks.
 *
 * Methods provided:
 *   findEmployeeById    – read by PK
 *   findAllEmployees    – read all rows
 *   saveEmployee        – insert new row
 *   updateEmployeeById  – update first/last name by PK
 *   deleteEmployeeById  – delete row by PK
 */
public class EmployeeDAO {

    // ------------------------------------------------------------------ READ

    /**
     * Find a single Employee by its primary key.
     *
     * @param id the employee ID
     * @return an Optional containing the Employee, or empty if not found
     */
    public Optional<Employee> findEmployeeById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employee employee = session.get(Employee.class, id);
            return Optional.ofNullable(employee);
        }
    }

    /**
     * Retrieve all employees from the database.
     *
     * @return list of all Employee records (may be empty, never null)
     */
    public List<Employee> findAllEmployees() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery(
                    "FROM Employee ORDER BY id", Employee.class);
            return query.list();
        }
    }

    // ----------------------------------------------------------------- WRITE

    /**
     * Persist a new Employee to the database.
     *
     * @param employee the Employee to insert (id is auto-generated)
     * @return the saved Employee with its generated id populated
     */
    public Employee saveEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
            return employee;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save employee", e);
        }
    }

    /**
     * Update the first name and last name of the employee with the given id.
     *
     * @param id        the employee ID to update
     * @param firstName new first name
     * @param lastName  new last name
     * @return true if a record was updated, false if the id was not found
     */
    public boolean updateEmployeeById(int id, String firstName, String lastName) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee == null) {
                transaction.rollback();
                return false;
            }
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            session.update(employee);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update employee id=" + id, e);
        }
    }

    /**
     * Delete the employee with the given id.
     *
     * @param id the employee ID to delete
     * @return true if a record was deleted, false if the id was not found
     */
    public boolean deleteEmployeeById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee == null) {
                transaction.rollback();
                return false;
            }
            session.delete(employee);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete employee id=" + id, e);
        }
    }
}
