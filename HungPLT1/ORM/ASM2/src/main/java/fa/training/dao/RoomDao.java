package fa.training.dao;

import fa.training.entities.CinemaRoom;
import java.util.List;

public interface RoomDao {
    boolean insertRoom(CinemaRoom room);
    CinemaRoom getRoomById(int id);
    List<CinemaRoom> getAllRoom();
    boolean updateRoomById(CinemaRoom room);
    boolean deleteRoomById(int id);
}
