package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import java.util.List;

public interface RoomDetailDao {
    boolean insertRoomDetail(CinemaRoomDetail roomDetail);
    CinemaRoomDetail getRoomDetailById(int roomDetailId);
    List<CinemaRoomDetail> getAllRoomDetails();
    boolean updateRoomDetailById(CinemaRoomDetail roomDetail);
    boolean deleteRoomDetailById(int roomDetailId);
}
