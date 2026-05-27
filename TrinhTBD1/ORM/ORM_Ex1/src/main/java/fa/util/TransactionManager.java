package fa.util;

import fa.config.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.function.Function;
import java.util.function.Consumer;

public class TransactionManager {

    public static <R> R execute(Function<EntityManager, R> action) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            R result = action.apply(em);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Database operation failed", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public static void executeVoid(Consumer<EntityManager> action) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Database operation failed", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
