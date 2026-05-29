package fa.training.dao.impl;

import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Optional;

public class RoomDetailDaoImpl extends GenericDaoImpl<CinemaRoomDetail, Integer> {

    public RoomDetailDaoImpl() {
        super(CinemaRoomDetail.class);
    }

    public Optional<CinemaRoomDetail> getByRoomId(int roomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM CinemaRoomDetail d WHERE d.cinemaRoom.cinemaRoomId = :roomId",
                    CinemaRoomDetail.class)
                    .setParameter("roomId", roomId)
                    .uniqueResultOptional();
        } catch (Exception ex) {
            log.error("Error in getByRoomId for CinemaRoomDetail roomId={}: {}", roomId, ex.getMessage(), ex);
            return Optional.empty();
        }
    }
}
