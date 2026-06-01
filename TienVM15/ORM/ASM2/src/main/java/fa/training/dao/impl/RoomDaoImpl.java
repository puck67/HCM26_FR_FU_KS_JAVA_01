package fa.training.dao.impl;

import fa.training.dao.RoomDao;
import fa.training.entities.CinemaRoom;
import java.util.List;

public class RoomDaoImpl extends GenericDAOImpl<CinemaRoom, Integer> implements RoomDao {

    public RoomDaoImpl() {
        super(CinemaRoom.class);
    }

    @Override
    protected void initLazyCollections(CinemaRoom room) {
        if (room != null) {
            if (room.getSeats() != null) {
                room.getSeats().size(); // force loading
            }
            if (room.getCinemaRoomDetail() != null) {
                room.getCinemaRoomDetail().toString(); // force loading
            }
        }
    }

    @Override
    public CinemaRoom getRoomByID(int id) {
        return findById(id);
    }

    @Override
    public List<CinemaRoom> getAllRooms() {
        return findAll();
    }

    @Override
    public void updateRoomByID(CinemaRoom room) {
        update(room);
    }

    @Override
    public void deleteRoomById(int id) {
        delete(id);
    }

    @Override
    public void insertRoom(CinemaRoom room) {
        save(room);
    }
}
