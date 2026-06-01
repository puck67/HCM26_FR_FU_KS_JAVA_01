package fa.training.main;

import fa.training.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class App {
    public static void main(String[] args) {
        // Create configuration and build session factory
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            // Problem 3: Create a new Employee object with selected first name and last name
            Employee employee = new Employee(1, "Long", "NTT8");
            
            // Use existing session to save new Employee object
            session.save(employee);
            
            transaction.commit();
            System.out.println("Employee saved successfully.");
            
            // Run 'SELECT * FROM EMPLOYEE' equivalent (HQL) to verify
            List<Employee> employees = session.createQuery("FROM Employee", Employee.class).list();
            System.out.println("\nEmployees in database (SELECT * FROM EMPLOYEE):");
            for (Employee emp : employees) {
                System.out.println(emp);
            }
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
}
