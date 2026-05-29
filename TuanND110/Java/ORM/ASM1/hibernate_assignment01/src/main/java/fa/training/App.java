package fa.training;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * App - Entry point for testing Hibernate Configuration and mapping Employee entity.
 */
public class App {

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("    HIBERNATE ASSIGNMENT 01 CONFIGURATION TEST");
        System.out.println("====================================================\n");

        // Problem 2: Configure Hibernate and test Configuration (Use SessionFactory in main)
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction transaction = null;

        try {
            System.out.println("[TEST 1] Testing SessionFactory Configuration...");
            sessionFactory = HibernateUtil.getSessionFactory();
            System.out.println("✓ SessionFactory initialized successfully!");

            // Open Session
            session = sessionFactory.openSession();
            System.out.println("✓ Database Session opened successfully!");

            // Problem 3: Create a new Employee object and save it using the session
            System.out.println("\n[TEST 2] Creating and saving a new Employee...");
            Employee employee = new Employee("Tuan", "Nguyen");
            
            transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
            System.out.println("✓ Employee 'Tuan Nguyen' saved successfully!");

            // Retrieve and display to verify
            System.out.println("\n[TEST 3] Running 'SELECT * FROM EMPLOYEE' query simulation...");
            Query<Employee> query = session.createQuery("FROM Employee", Employee.class);
            List<Employee> employees = query.list();
            
            System.out.println("Retrieved Employees from database:");
            for (Employee emp : employees) {
                System.out.println("  - ID: " + emp.getId() + " | First Name: " + emp.getFirstName() + " | Last Name: " + emp.getLastName());
            }
            
            System.out.println("\nConfiguration and Mapping verify successful! Log output has NO errors.");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Configuration test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
            HibernateUtil.shutdown();
        }
    }
}
