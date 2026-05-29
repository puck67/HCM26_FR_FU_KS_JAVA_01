package fa.training.dao.impl;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDAOImpl.class);

    @Override
    public Employee getEmployeeByID(int id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            transaction.commit();
            return employee;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    logger.debug("Failed to rollback transaction", rollbackEx);
                }
            }
            logger.error("Error retrieving employee by ID: " + id, e);
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Employee> getAllEmployee() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            List<Employee> employees = session.createQuery("from Employee", Employee.class).getResultList();
            transaction.commit();
            return employees;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    logger.debug("Failed to rollback transaction", rollbackEx);
                }
            }
            logger.error("Error retrieving all employees", e);
            return new ArrayList<>();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean updateEmployeeByID(Employee employee) {
        if (employee == null) {
            return false;
        }
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Employee existing = session.get(Employee.class, employee.getId());
            if (existing == null) {
                logger.warn("Attempted to update non-existent Employee ID: " + employee.getId());
                transaction.commit();
                return false;
            }
            existing.setFirstName(employee.getFirstName());
            existing.setLastName(employee.getLastName());
            session.merge(existing);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    logger.debug("Failed to rollback transaction", rollbackEx);
                }
            }
            logger.error("Error updating employee: " + employee, e);
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean deleteEmployeeByID(int id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee == null) {
                logger.warn("Attempted to delete non-existent Employee ID: " + id);
                transaction.commit();
                return false;
            }
            session.remove(employee);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    logger.debug("Failed to rollback transaction", rollbackEx);
                }
            }
            logger.error("Error deleting employee ID: " + id, e);
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean insertEmployee(Employee employee) {
        if (employee == null) {
            return false;
        }
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    logger.debug("Failed to rollback transaction", rollbackEx);
                }
            }
            logger.error("Error inserting employee: " + employee, e);
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
