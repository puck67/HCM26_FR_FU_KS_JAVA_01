package fa.training.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class to provide a singleton SessionFactory.
 * SessionFactory is expensive to create, so we only create it once.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    // Private constructor to prevent instantiation
    private HibernateUtil() {}

    /**
     * Returns the singleton SessionFactory.
     * Builds it from hibernate.cfg.xml on first call.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Loads configuration from src/main/resources/hibernate.cfg.xml
                sessionFactory = new Configuration()
                        .configure("hibernate.cfg.xml")
                        .buildSessionFactory();
            } catch (Exception e) {
                System.err.println("Failed to create SessionFactory: " + e.getMessage());
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    /**
     * Closes the SessionFactory when application shuts down.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
