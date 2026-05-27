package fa.training.dao.impl;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class EmployeeDAOImpl implements EmployeeDAO {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDAOImpl.class);

    @Override
    public Optional<Employee> findEmployeeById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Employee.class, id));
        } catch (Exception e) {
            logger.error("findEmployeeById({}): {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<Employee> findAllEmployees() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Employee ORDER BY id", Employee.class)
                    .getResultList();
        } catch (Exception e) {
            logger.error("findAllEmployees: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public Employee insertEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
            return employee;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("insertEmployee: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to insert employee", e);
        }
    }

    @Override
    public boolean updateEmployeeById(int id, String firstName, String lastName) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Employee employee = session.get(Employee.class, id);

            String action = switch (employee != null ? "found" : "not_found") {
                case "found" -> "proceed";
                default -> "skip";
            };

            if ("skip".equals(action)) {
                transaction.rollback();
                return false;
            }

            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            session.merge(employee);
            transaction.commit();
            return true;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("updateEmployeeById({}): {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteEmployeeById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Employee employee = session.get(Employee.class, id);
            if (employee == null) {
                transaction.rollback();
                return false;
            }

            session.remove(employee);
            transaction.commit();
            return true;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("deleteEmployeeById({}): {}", id, e.getMessage(), e);
            return false;
        }
    }
}
