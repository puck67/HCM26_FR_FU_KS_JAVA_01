package fa.training.util;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class HibernateUtil {

    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);
    private static volatile SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        Properties dbProps = loadDbProperties();

                        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                                .configure()
                                .applySetting(Environment.DRIVER, dbProps.getProperty("db.driver"))
                                .applySetting(Environment.URL,    dbProps.getProperty("db.url"))
                                .applySetting(Environment.USER,   dbProps.getProperty("db.username"))
                                .applySetting(Environment.PASS,   dbProps.getProperty("db.password"))
                                .build();

                        sessionFactory = new MetadataSources(registry)
                                .addAnnotatedClass(CinemaRoom.class)
                                .addAnnotatedClass(CinemaRoomDetail.class)
                                .addAnnotatedClass(Seat.class)
                                .buildMetadata()
                                .buildSessionFactory();

                        log.info("Hibernate SessionFactory initialized successfully.");
                    } catch (Exception ex) {
                        log.error("Failed to initialize Hibernate SessionFactory: {}", ex.getMessage(), ex);
                        throw new ExceptionInInitializerError(ex);
                    }
                }
            }
        }
        return sessionFactory;
    }

    private static Properties loadDbProperties() {
        Properties props = new Properties();
        try (InputStream in = HibernateUtil.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IOException("db.properties not found on classpath.");
            }
            props.load(in);
            log.info("Loaded db.properties successfully.");
        } catch (IOException ex) {
            log.error("Failed to load db.properties: {}", ex.getMessage(), ex);
            throw new RuntimeException("Cannot load db.properties", ex);
        }
        return props;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            log.info("Hibernate SessionFactory closed.");
        }
    }
}
