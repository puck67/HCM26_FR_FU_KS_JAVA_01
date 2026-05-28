package fa.training.dao.impl;

import fa.training.entities.CinemaRoom;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoomDaoImpl extends GenericDaoImpl<CinemaRoom, Integer> {

    public RoomDaoImpl() {
        super(CinemaRoom.class);
    }

    public Optional<CinemaRoom> getByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM CinemaRoom r WHERE r.cinemaRoomName = :name", CinemaRoom.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        } catch (Exception ex) {
            log.error("Error in getByName for CinemaRoom name='{}': {}", name, ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    public List<CinemaRoom> getAllWithDetails() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT DISTINCT r FROM CinemaRoom r LEFT JOIN FETCH r.cinemaRoomDetail",
                    CinemaRoom.class)
                    .list();
        } catch (Exception ex) {
            log.error("Error in getAllWithDetails for CinemaRoom: {}", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }
}
