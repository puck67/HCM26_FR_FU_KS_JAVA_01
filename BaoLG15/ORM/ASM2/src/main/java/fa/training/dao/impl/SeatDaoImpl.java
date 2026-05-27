package fa.training.dao.impl;

import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

public class SeatDaoImpl extends GenericDaoImpl<Seat, Integer> {

    public SeatDaoImpl() {
        super(Seat.class);
    }

    public List<Seat> getByRoomId(int roomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Seat s WHERE s.cinemaRoom.cinemaRoomId = :roomId", Seat.class)
                    .setParameter("roomId", roomId)
                    .list();
        } catch (Exception ex) {
            log.error("Error in getByRoomId for Seat roomId={}: {}", roomId, ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    public List<Seat> getByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Seat s WHERE s.seatStatus = :status", Seat.class)
                    .setParameter("status", status)
                    .list();
        } catch (Exception ex) {
            log.error("Error in getByStatus for Seat status='{}': {}", status, ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    public List<Seat> getByType(String type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Seat s WHERE s.seatType = :type", Seat.class)
                    .setParameter("type", type)
                    .list();
        } catch (Exception ex) {
            log.error("Error in getByType for Seat type='{}': {}", type, ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }
}
