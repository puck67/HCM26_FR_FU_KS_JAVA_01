package fa.training.service;

import fa.training.dao.RoomDetailDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import java.time.LocalDate;
import java.util.List;

public class RoomDetailService {
    private final RoomDetailDao detailDao = new RoomDetailDao();

    public void addRoomDetail(int roomRate, LocalDate activeDate, String roomDescription, CinemaRoom room) {
        CinemaRoomDetail detail = new CinemaRoomDetail(roomRate, activeDate, roomDescription, room);
        detailDao.insertRoomDetail(detail);
    }

    public List<CinemaRoomDetail> getAllRoomDetails() {
        return detailDao.getAllRoomDetail();
    }
}
