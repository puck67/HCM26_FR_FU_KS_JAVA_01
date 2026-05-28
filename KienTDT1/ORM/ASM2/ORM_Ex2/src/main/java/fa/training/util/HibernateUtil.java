package fa.training.util;

import fa.training.entity.Course;
import fa.training.entity.Student;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;



public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Student.class)
                    .addAnnotatedClass(Course.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }
}