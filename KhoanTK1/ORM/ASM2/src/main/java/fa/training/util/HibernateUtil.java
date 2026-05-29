package fa.training.util;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import java.io.InputStream;
import java.util.Properties;

public final class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try {
                // Doc thong tin db tu file properties
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

                System.out.println("Khoi tao SessionFactory thanh cong!");
            } catch (Exception ex) {
                System.err.println("Loi khi khoi tao SessionFactory: " + ex.getMessage());
                ex.printStackTrace();
                throw new ExceptionInInitializerError(ex);
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
                throw new RuntimeException("Loi: Khong tim thay file db.properties tren classpath!");
            }
            props.load(in);
        } catch (Exception ex) {
            System.err.println("Loi khi tai db.properties: " + ex.getMessage());
            throw new RuntimeException("Khong the doc file db.properties", ex);
        }
        return props;
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("Da dong Hibernate SessionFactory.");
        }
    }
}
