package fa.training.dao;

import fa.training.entities.CinemaRoom;

public class RoomDao extends AbstractDao<CinemaRoom, Integer> {
    public RoomDao() {
        super(CinemaRoom.class);
    }
}
