package fa.training.dao;

import fa.training.entities.CinemaRoom;
import java.util.List;

public interface RoomDao {
    boolean insertRoom(CinemaRoom room);
    CinemaRoom getRoomByID(int id);
    List<CinemaRoom> getAllRoom();
    boolean updateRoomByID(int id, CinemaRoom room);
    boolean deleteRoomById(int id);
}
