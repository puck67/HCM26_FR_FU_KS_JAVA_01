package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import java.util.List;

public interface RoomDetailDao {
    boolean insertRoomDetail(CinemaRoomDetail roomDetail);
    CinemaRoomDetail getRoomDetailById(int id);
    List<CinemaRoomDetail> getAllRoomDetails();
    boolean updateRoomDetailById(int id, CinemaRoomDetail roomDetail);
    boolean deleteRoomDetailById(int id);
}
