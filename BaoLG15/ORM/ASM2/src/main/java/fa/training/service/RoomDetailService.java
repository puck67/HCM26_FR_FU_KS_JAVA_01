package fa.training.service;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RoomDetailService {

    private static final Logger log = LoggerFactory.getLogger(RoomDetailService.class);
    private final RoomDetailDaoImpl roomDetailDao = new RoomDetailDaoImpl();
    private final RoomDaoImpl roomDao = new RoomDaoImpl();

    public List<CinemaRoomDetail> getAllDetails() {
        return roomDetailDao.getAll();
    }

    public Optional<CinemaRoomDetail> getDetailById(int id) {
        return roomDetailDao.getById(id);
    }

    public Optional<CinemaRoomDetail> getDetailByRoomId(int roomId) {
        return roomDetailDao.getByRoomId(roomId);
    }

    public void addDetail(int roomId, int roomRate, String activeDateStr, String description) {
        Validator.requirePositive(roomRate, "Room rate");
        LocalDate activeDate = Validator.parseDate(activeDateStr);

        CinemaRoom room = roomDao.getById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Cinema room with id=" + roomId + " not found."));

        roomDetailDao.getByRoomId(roomId).ifPresent(d -> {
            throw new IllegalArgumentException("Room id=" + roomId + " already has a detail record.");
        });

        CinemaRoomDetail detail = new CinemaRoomDetail(room, roomRate, activeDate,
                description == null ? "" : description.trim());
        roomDetailDao.insert(detail);
        log.info("RoomDetailService: added detail for roomId={}", roomId);
    }

    public void updateDetail(int detailId, int roomRate, String activeDateStr, String description) {
        Validator.requirePositive(roomRate, "Room rate");
        LocalDate activeDate = Validator.parseDate(activeDateStr);

        CinemaRoomDetail detail = roomDetailDao.getById(detailId)
                .orElseThrow(() -> new IllegalArgumentException("Room detail with id=" + detailId + " not found."));

        detail.setRoomRate(roomRate);
        detail.setActiveDate(activeDate);
        detail.setRoomDescription(description == null ? "" : description.trim());
        roomDetailDao.updateById(detail);
        log.info("RoomDetailService: updated detail id={}", detailId);
    }

    public void deleteDetail(int id) {
        roomDetailDao.deleteById(id);
        log.info("RoomDetailService: deleted detail id={}", id);
    }
}
