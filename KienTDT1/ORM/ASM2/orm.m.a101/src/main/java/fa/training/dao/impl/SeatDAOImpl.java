package fa.training.dao.impl;

import fa.training.dao.SeatDAO;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SeatDAOImpl implements SeatDAO {

    @Override
    public void insert(Seat seat) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();

        session.persist(seat);

        tx.commit();

        session.close();
    }

    @Override
    public Seat getById(int id) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Seat seat = session.get(Seat.class, id);

        session.close();

        return seat;
    }

    @Override
    public List<Seat> getAll() {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        List<Seat> list =
                session.createQuery("FROM Seat", Seat.class)
                        .list();

        session.close();

        return list;
    }

    @Override
    public void update(Seat seat) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();

        session.merge(seat);

        tx.commit();

        session.close();
    }

    @Override
    public void delete(int id) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();

        Seat seat = session.get(Seat.class, id);

        if (seat != null) {
            session.remove(seat);
        }

        tx.commit();

        session.close();
    }
}