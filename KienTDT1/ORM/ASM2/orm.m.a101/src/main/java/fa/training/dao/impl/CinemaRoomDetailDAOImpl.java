package fa.training.dao.impl;

import fa.training.dao.CinemaRoomDetailDAO;
import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CinemaRoomDetailDAOImpl
        implements CinemaRoomDetailDAO {

    @Override
    public void insert(CinemaRoomDetail detail) {

        Session session =
                HibernateUtil.getSessionFactory()
                        .openSession();

        Transaction tx =
                session.beginTransaction();

        session.persist(detail);

        tx.commit();

        session.close();
    }

    @Override
    public CinemaRoomDetail getById(int id) {

        Session session =
                HibernateUtil.getSessionFactory()
                        .openSession();

        CinemaRoomDetail detail =
                session.get(
                        CinemaRoomDetail.class,
                        id);

        session.close();

        return detail;
    }

    @Override
    public List<CinemaRoomDetail> getAll() {

        Session session =
                HibernateUtil.getSessionFactory()
                        .openSession();

        List<CinemaRoomDetail> list =
                session.createQuery(
                                "FROM CinemaRoomDetail",
                                CinemaRoomDetail.class)
                        .list();

        session.close();

        return list;
    }

    @Override
    public void update(CinemaRoomDetail detail) {

        Session session =
                HibernateUtil.getSessionFactory()
                        .openSession();

        Transaction tx =
                session.beginTransaction();

        session.merge(detail);

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

        CinemaRoomDetail detail =
                session.get(
                        CinemaRoomDetail.class,
                        id);

        if (detail != null) {
            session.remove(detail);
        }

        tx.commit();

        session.close();
    }
}