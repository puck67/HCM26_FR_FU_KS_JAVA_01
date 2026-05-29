package fa.training.dao;

import fa.training.entities.Employee;
import fa.training.util.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Employee entity.
 */
public class EmployeeDAO {
    private static final Logger logger = Logger.getLogger(EmployeeDAO.class.getName());

    /**
     * Insert a new Employee into the database.
     */
    public boolean save(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error saving employee", e);
        }
        return false;
    }

    /**
     * Get an Employee by ID.
     */
    public Optional<Employee> findById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Employee.class, id));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding employee by id: " + id, e);
        }
        return Optional.empty();
    }

    /**
     * Get all Employees.
     */
    public List<Employee> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery("from Employee", Employee.class);
            return query.list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving all employees", e);
        }
        return Collections.emptyList();
    }

    /**
     * Update an existing Employee.
     */
    public boolean update(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(employee);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete an Employee by ID.
     */
    public boolean delete(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                transaction = session.beginTransaction();
                session.delete(employee);
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }
}
