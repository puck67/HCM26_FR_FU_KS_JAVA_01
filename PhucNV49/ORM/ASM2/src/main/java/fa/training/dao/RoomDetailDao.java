package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import java.util.List;

public interface RoomDetailDao {
    boolean insertRoomDetail(CinemaRoomDetail roomDetail);
    CinemaRoomDetail getRoomDetailByID(int id);
    List<CinemaRoomDetail> getAllRoomDetail();
    boolean updateRoomDetailByID(CinemaRoomDetail roomDetail);
    boolean deleteRoomDetailById(int id);
}
