package fa.training.dao;

import fa.training.entities.CinemaRoom;
import java.util.List;

public interface RoomDao extends GenericDAO<CinemaRoom, Integer> {
    CinemaRoom getRoomByID(int id);
    List<CinemaRoom> getAllRooms();
    void updateRoomByID(CinemaRoom room);
    void deleteRoomById(int id);
    void insertRoom(CinemaRoom room);
}
