package fa.training.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Singleton SessionFactory - initialized only once for the application lifecycle
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
