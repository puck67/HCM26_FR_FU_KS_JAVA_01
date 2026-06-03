package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import java.util.List;

public interface RoomDetailDao {
    boolean insertRoomDetail(CinemaRoomDetail detail);
    CinemaRoomDetail getRoomDetailById(int id);
    List<CinemaRoomDetail> getAllRoomDetail();
    boolean updateRoomDetailById(CinemaRoomDetail detail);
    boolean deleteRoomDetailById(int id);
}
