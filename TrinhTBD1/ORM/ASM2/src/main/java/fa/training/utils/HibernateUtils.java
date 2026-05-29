package fa.training.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtils {
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

            Properties props = new Properties();
            try (InputStream input = HibernateUtils.class.getClassLoader()
                    .getResourceAsStream("database.properties")) {
                if (input != null) {
                    props.load(input);

                    if (props.getProperty("db.driver") != null) {
                        configuration.setProperty("hibernate.connection.driver_class", props.getProperty("db.driver"));
                    }
                    if (props.getProperty("db.url") != null) {
                        configuration.setProperty("hibernate.connection.url", props.getProperty("db.url"));
                    }
                    if (props.getProperty("db.username") != null) {
                        configuration.setProperty("hibernate.connection.username", props.getProperty("db.username"));
                    }
                    if (props.getProperty("db.password") != null) {
                        configuration.setProperty("hibernate.connection.password", props.getProperty("db.password"));
                    }
                    if (props.getProperty("db.dialect") != null) {
                        configuration.setProperty("hibernate.dialect", props.getProperty("db.dialect"));
                    }
                    if (props.getProperty("db.hbm2ddl.auto") != null) {
                        configuration.setProperty("hibernate.hbm2ddl.auto", props.getProperty("db.hbm2ddl.auto"));
                    }
                    if (props.getProperty("db.show_sql") != null) {
                        configuration.setProperty("hibernate.show_sql", props.getProperty("db.show_sql"));
                    }
                    if (props.getProperty("db.format_sql") != null) {
                        configuration.setProperty("hibernate.format_sql", props.getProperty("db.format_sql"));
                    }
                }
            } catch (Exception e) {
                System.err.println(new StringBuilder()
                        .append("Warning: Could not load database.properties. Using default hibernate.cfg.xml settings. Reason: ")
                        .append(e.getMessage()));
            }

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println(new StringBuilder()
                    .append("Initial SessionFactory creation failed: ")
                    .append(ex.getMessage()));
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            synchronized (HibernateUtils.class) {
                if (sessionFactory == null || sessionFactory.isClosed()) {
                    sessionFactory = buildSessionFactory();
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
