package com.practice.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    static {
        // Suppress SLF4J simple logger outputs
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
        // Suppress Java Util Logging (JUL) outputs used by Hibernate
        java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.SEVERE);
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            String configFile = System.getProperty("hibernate.config.file", "hibernate.cfg.xml");
            Configuration configuration = new Configuration().configure(configFile);

            // Support environment variable overrides for database connection
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
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
