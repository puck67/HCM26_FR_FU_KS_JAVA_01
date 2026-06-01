package fa.training.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class to manage Hibernate SessionFactory.
 * SessionFactory is thread-safe and should be created only once (Singleton pattern).
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    // Private constructor - prevents instantiation
    private HibernateUtil() {}

    /**
     * Returns the singleton SessionFactory instance.
     * Lazily initializes on first call.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Load configuration from hibernate.cfg.xml
                Configuration configuration = new Configuration().configure();
                sessionFactory = configuration.buildSessionFactory();
                System.out.println("SessionFactory created successfully.");
            } catch (Exception e) {
                System.err.println("Failed to create SessionFactory: " + e.getMessage());
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    /**
     * Closes the SessionFactory and releases all resources.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("SessionFactory closed.");
        }
    }
}
