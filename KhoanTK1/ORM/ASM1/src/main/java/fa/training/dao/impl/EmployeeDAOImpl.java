package fa.training.dao.impl;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public Optional<Employee> findEmployeeById(int id) {
        // Lay session tu SessionFactory
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employee employee = session.get(Employee.class, id);
            return Optional.ofNullable(employee);
        } catch (Exception e) {
            System.err.println("Loi khi tim nhan vien bang ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Employee> findAllEmployees() {
        // Doc toan bo danh sach nhan vien tu db
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Employee ORDER BY id", Employee.class).getResultList();
        } catch (Exception e) {
            System.err.println("Loi khi lay danh sach nhan vien: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Employee insertEmployee(Employee employee) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
            return employee;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi khi them nhan vien: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateEmployeeById(int id, String firstName, String lastName) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            
            // Tim employee truoc
            Employee employee = session.get(Employee.class, id);
            if (employee == null) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                return false;
            }
            
            // Update cac truong thong tin
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            
            session.merge(employee);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi khi cap nhat nhan vien: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteEmployeeById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            
            Employee employee = session.get(Employee.class, id);
            if (employee == null) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                return false;
            }
            
            session.remove(employee);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Loi khi xoa nhan vien: " + e.getMessage());
            return false;
        }
    }
}
