package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;
import java.util.List;

public interface RoomDetailDao extends GenericDAO<CinemaRoomDetail, Integer> {
    CinemaRoomDetail getRoomDetailByID(int id);
    List<CinemaRoomDetail> getAllRoomDetails();
    void updateRoomDetailByID(CinemaRoomDetail detail);
    void deleteRoomDetailById(int id);
    void insertRoomDetail(CinemaRoomDetail detail);
}
