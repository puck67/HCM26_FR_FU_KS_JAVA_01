package fa.training.dao.impl;

import fa.training.dao.RoomDetailDao;
import fa.training.entities.CinemaRoomDetail;
import java.util.List;

public class RoomDetailDaoImpl extends GenericDAOImpl<CinemaRoomDetail, Integer> implements RoomDetailDao {

    public RoomDetailDaoImpl() {
        super(CinemaRoomDetail.class);
    }

    @Override
    protected void initLazyCollections(CinemaRoomDetail detail) {
        if (detail != null && detail.getCinemaRoom() != null) {
            detail.getCinemaRoom().getCinemaRoomName(); // force loading
        }
    }

    @Override
    public CinemaRoomDetail getRoomDetailByID(int id) {
        return findById(id);
    }

    @Override
    public List<CinemaRoomDetail> getAllRoomDetails() {
        return findAll();
    }

    @Override
    public void updateRoomDetailByID(CinemaRoomDetail detail) {
        update(detail);
    }

    @Override
    public void deleteRoomDetailById(int id) {
        delete(id);
    }

    @Override
    public void insertRoomDetail(CinemaRoomDetail detail) {
        save(detail);
    }
}
