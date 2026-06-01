package fa.training.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
    }

    private static void suppressHibernateLogs() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        if (rootLogger != null) {
            rootLogger.setLevel(Level.SEVERE);
            for (Handler handler : rootLogger.getHandlers()) {
                handler.setLevel(Level.SEVERE);
            }
        }

        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate.SQL").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate.type.descriptor.sql.BasicBinder").setLevel(Level.SEVERE);
        Logger.getLogger("org.jboss.logging").setLevel(Level.SEVERE);
    }

    private static SessionFactory buildSessionFactory() {
        try {
            suppressHibernateLogs();

            // Register shutdown hook to close the SessionFactory cleanly.
            Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));

            return new Configuration().configure().buildSessionFactory();
        } catch (Exception ex) {
            throw new ExceptionInInitializerError("Failed to create SessionFactory: " + ex.getMessage());
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        try {
            if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
                SESSION_FACTORY.close();
            }
        } catch (Exception ignored) {
        }
    }
}
