package fa.training.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // 1. Load connection properties from external properties file to avoid hard-coding
            Properties props = new Properties();
            try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (input == null) {
                    throw new RuntimeException("Sorry, unable to find db.properties in resources.");
                }
                props.load(input);
            } catch (IOException ex) {
                logger.error("Failed to load db.properties", ex);
                throw new ExceptionInInitializerError("Could not load db.properties: " + ex.getMessage());
            }

            // 2. Build Hibernate Configuration using hibernate.cfg.xml (for mappings)
            Configuration configuration = new Configuration();
            configuration.configure(); // loads hibernate.cfg.xml

            // 3. Inject database connection settings programmatically from db.properties
            configuration.setProperty("hibernate.connection.driver_class", props.getProperty("db.driver"));
            configuration.setProperty("hibernate.connection.url", props.getProperty("db.url"));
            configuration.setProperty("hibernate.connection.username", props.getProperty("db.username"));
            configuration.setProperty("hibernate.connection.password", props.getProperty("db.password"));
            configuration.setProperty("hibernate.dialect", props.getProperty("db.dialect"));
            configuration.setProperty("hibernate.hbm2ddl.auto", props.getProperty("db.hbm2ddl.auto"));
            configuration.setProperty("hibernate.show_sql", props.getProperty("db.show_sql"));
            configuration.setProperty("hibernate.format_sql", props.getProperty("db.format_sql"));

            // 4. Build SessionFactory
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            logger.info("SessionFactory closed successfully.");
        }
    }
}
