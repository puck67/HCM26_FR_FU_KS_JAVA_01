package fa.training.service;

import fa.training.dao.RoomDAO;
import fa.training.dao.RoomDetailDAO;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.util.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RoomDetailService {

    private final RoomDetailDAO roomDetailDao = new RoomDetailDAO();
    private final RoomDAO roomDao = new RoomDAO();

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
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay phong chieu voi ID = " + roomId));

        if (roomDetailDao.getByRoomId(roomId).isPresent()) {
            throw new IllegalArgumentException("Phong chieu ID = " + roomId + " da co ban ghi chi tiet room detail roi.");
        }

        CinemaRoomDetail detail = new CinemaRoomDetail(room, roomRate, activeDate,
                description == null ? "" : description.trim());
        roomDetailDao.insert(detail);
    }

    public void updateDetail(int detailId, int roomRate, String activeDateStr, String description) {
        Validator.requirePositive(roomRate, "Room rate");
        LocalDate activeDate = Validator.parseDate(activeDateStr);

        CinemaRoomDetail detail = roomDetailDao.getById(detailId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay chi tiet phong chieu voi ID = " + detailId));

        detail.setRoomRate(roomRate);
        detail.setActiveDate(activeDate);
        detail.setRoomDescription(description == null ? "" : description.trim());
        roomDetailDao.update(detail);
    }

    public void deleteDetail(int id) {
        roomDetailDao.deleteById(id);
    }
}
