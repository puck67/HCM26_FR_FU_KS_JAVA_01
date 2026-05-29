package fa.training.dao;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;


public class EmployeeDAO {

    @FunctionalInterface
    private interface SessionCallback<T> {
        T doInSession(Session session) throws Exception;
    }


    @FunctionalInterface
    private interface SessionVoidCallback {
        void doInSession(Session session) throws Exception;
    }

    private <T> T execute(SessionCallback<T> callback) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T result = callback.doInSession(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Database operation failed: " + e.getMessage(), e);
        }
    }

    private void executeVoid(SessionVoidCallback callback) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            callback.doInSession(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Database operation failed: " + e.getMessage(), e);
        }
    }

    public void insertEmployee(Employee employee) {
        executeVoid(session -> session.save(employee));
    }


    public Optional<Employee> getEmployeeByID(int id) {
        return execute(session -> Optional.ofNullable(session.get(Employee.class, id)));
    }

    public List<Employee> getAllEmployee() {
        return execute(session -> session.createQuery("from Employee", Employee.class).list());
    }

    public void updateEmployeeByID(int id, Employee updatedEmployee) {
        executeVoid(session -> {
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                employee.setFirstName(updatedEmployee.getFirstName());
                employee.setLastName(updatedEmployee.getLastName());
                session.update(employee);
            } else {
                System.out.println("[DAO] Employee with ID " + id + " not found for update.");
            }
        });
    }

    public void deleteEmployeeById(int id) {
        executeVoid(session -> {
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
            } else {
                System.out.println("[DAO] Employee with ID " + id + " not found for deletion.");
            }
        });
    }
}
