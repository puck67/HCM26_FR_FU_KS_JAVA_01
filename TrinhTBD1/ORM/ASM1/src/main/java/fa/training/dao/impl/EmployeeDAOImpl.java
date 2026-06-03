package fa.training.dao.impl;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public boolean insertEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println(new StringBuilder()
                    .append("Error inserting employee: ")
                    .append(e.getMessage()));
            return false;
        }
    }

    @Override
    public Employee getEmployeeByID(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        } catch (Exception e) {
            System.err.println(new StringBuilder()
                    .append("Error getting employee by ID: ")
                    .append(e.getMessage()));
            return null;
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery("from Employee", Employee.class);
            return query.list();
        } catch (Exception e) {
            System.err.println(new StringBuilder()
                    .append("Error getting all employees: ")
                    .append(e.getMessage()));
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateEmployeeByID(int id, Employee updatedEmployee) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee existing = session.get(Employee.class, id);
            if (existing != null) {
                existing.setFirstName(updatedEmployee.getFirstName());
                existing.setLastName(updatedEmployee.getLastName());
                session.merge(existing);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println(new StringBuilder()
                    .append("Error updating employee by ID: ")
                    .append(e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean deleteEmployeeByID(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.remove(employee);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println(new StringBuilder()
                    .append("Error deleting employee by ID: ")
                    .append(e.getMessage()));
            return false;
        }
    }
}
