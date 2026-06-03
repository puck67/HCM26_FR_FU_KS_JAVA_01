package fa.training;

import fa.training.entities.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Problem 2 & 3 - Tests Hibernate configuration and saves a new Employee.
 * Verifies connection to the 'fadb' H2 database and that the EMPLOYEE table
 * is created and populated correctly.
 */
public class App {

    public static void main(String[] args) {

        // --- Problem 2: Build SessionFactory and verify connection ---
        System.out.println("=== Building Hibernate SessionFactory ===");

        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        System.out.println("=== SessionFactory created successfully — connected to fadb ===");

        // --- Problem 3: Persist a new Employee ---
        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            Employee emp = new Employee("John", "Doe");
            session.persist(emp);

            session.getTransaction().commit();

            System.out.println("=== Employee saved: " + emp + " ===");
        }

        sessionFactory.close();
        System.out.println("=== Done. Run: SELECT * FROM EMPLOYEE to verify. ===");
    }
}
