package fa.training.dao.impl;

import fa.training.dao.CinemaRoomDAO;
import fa.training.entities.CinemaRoom;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CinemaRoomDAOImpl
        implements CinemaRoomDAO {

    @Override
    public void insert(CinemaRoom room) {

        Session session =
                HibernateUtil.getSessionFactory()
                        .openSession();

        Transaction tx =
                session.beginTransaction();

        session.persist(room);

        tx.commit();

        session.close();
    }

    @Override
    public CinemaRoom getById(int id) {

        Session session =
                HibernateUtil.getSessionFactory()
                        .openSession();

        CinemaRoom room =
                session.get(CinemaRoom.class, id);

        session.close();

        return room;
    }

    @Override
    public List<CinemaRoom> getAll() {

        Session session =
                HibernateUtil.getSessionFactory()
                        .openSession();

        List<CinemaRoom> list =
                session.createQuery(
                                "FROM CinemaRoom",
                                CinemaRoom.class)
                        .list();

        session.close();

        return list;
    }

    @Override
    public void update(CinemaRoom room) {

        Session session =
                HibernateUtil.getSessionFactory()
                        .openSession();

        Transaction tx =
                session.beginTransaction();

        session.merge(room);

        tx.commit();

        session.close();
    }

    @Override
    public void delete(int id) {

        Session session =
                HibernateUtil.getSessionFactory()
                        .openSession();

        Transaction tx =
                session.beginTransaction();

        CinemaRoom room =
                session.get(CinemaRoom.class, id);

        if (room != null) {
            session.remove(room);
        }

        tx.commit();

        session.close();
    }
}