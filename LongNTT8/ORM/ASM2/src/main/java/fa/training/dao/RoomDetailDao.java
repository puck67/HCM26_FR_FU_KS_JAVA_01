package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;

public class RoomDetailDao extends AbstractDao<CinemaRoomDetail, Integer> {
    public RoomDetailDao() {
        super(CinemaRoomDetail.class);
    }
}
