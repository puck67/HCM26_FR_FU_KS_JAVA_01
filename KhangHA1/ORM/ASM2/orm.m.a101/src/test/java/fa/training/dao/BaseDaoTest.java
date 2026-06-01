package fa.training.dao;

import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.Before;

public abstract class BaseDaoTest {

    @Before
    public void setUp() {
        cleanupDatabase();
    }

    protected void cleanupDatabase() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            // Delete in order of dependencies (child tables first)
            session.createMutationQuery("delete from Seat").executeUpdate();
            session.createMutationQuery("delete from CinemaRoomDetail").executeUpdate();
            session.createMutationQuery("delete from CinemaRoom").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
