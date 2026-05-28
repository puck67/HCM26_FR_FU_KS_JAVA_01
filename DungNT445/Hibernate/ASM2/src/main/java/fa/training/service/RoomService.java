package fa.training.service;

import fa.training.dao.RoomDao;
import fa.training.entities.CinemaRoom;
import java.util.List;

public class RoomService {
    private final RoomDao roomDao = new RoomDao();

    public void createRoom(String name, int seatQuantity) {
        CinemaRoom room = new CinemaRoom(name, seatQuantity);
        roomDao.insertRoom(room);
    }

    public List<CinemaRoom> getAllRooms() {
        return roomDao.getAllRoom();
    }

    public CinemaRoom getRoomById(int id) {
        return roomDao.getRoomById(id);
    }
}
