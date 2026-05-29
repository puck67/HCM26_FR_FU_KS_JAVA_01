package fa.training.dao;

import fa.training.entities.CinemaRoom;
import java.util.List;

public interface RoomDao {
    boolean insertRoom(CinemaRoom room);
    CinemaRoom getRoomById(int id);
    List<CinemaRoom> getAllRooms();
    boolean updateRoomById(int id, CinemaRoom room);
    boolean deleteRoomById(int id);
}
