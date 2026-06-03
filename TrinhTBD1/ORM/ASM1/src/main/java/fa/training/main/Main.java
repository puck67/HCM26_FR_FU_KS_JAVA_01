package fa.training.main;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Main {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("     Initializing Hibernate Session Factory...   ");
        System.out.println("=================================================");

        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        
        System.out.println("\n--- Problem 3: Saving Employee using Session ---");
        
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            Employee employee = new Employee("Adam", "Smith");
            session.persist(employee);
            tx.commit();
            
            System.out.println(new StringBuilder()
                    .append("Successfully connected to fadb database.\n")
                    .append("New Employee saved successfully: ")
                    .append(employee)
                    .toString());
            
        } catch (Exception e) {
            System.err.println(new StringBuilder()
                    .append("Error during Hibernate execution: ")
                    .append(e.getMessage())
                    .toString());
        } finally {
            HibernateUtils.shutdown();
        }
    }
}
