package com.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * HibernateUtil manages the Hibernate SessionFactory lifecycle.
 * - Silences SLF4J and JUL logging at startup.
 * - Supports overriding DB connection via env variables (DB_URL, DB_USER, DB_PASSWORD).
 * - Supports selecting an alternate config file via system property "hibernate.config.file".
 */
public class HibernateUtil {

    static {
        // Suppress SLF4J simple logger outputs globally
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
        // Suppress Java Util Logging (JUL) used internally by Hibernate
        java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.SEVERE);
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Allow test or alternate configurations via system property
            String configFile = System.getProperty("hibernate.config.file", "hibernate.cfg.xml");
            Configuration configuration = new Configuration().configure(configFile);

            // Override database connection via environment variables (useful for CI/CD)
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            if (dbUrl != null && !dbUrl.trim().isEmpty()) {
                configuration.setProperty("hibernate.connection.url", dbUrl);
            }
            if (dbUser != null && !dbUser.trim().isEmpty()) {
                configuration.setProperty("hibernate.connection.username", dbUser);
            }
            if (dbPassword != null && !dbPassword.trim().isEmpty()) {
                configuration.setProperty("hibernate.connection.password", dbPassword);
            }

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /** Cleanly close the SessionFactory on application exit */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
