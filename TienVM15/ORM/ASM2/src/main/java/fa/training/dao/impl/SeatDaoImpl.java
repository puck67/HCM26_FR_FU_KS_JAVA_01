package fa.training.dao.impl;

import fa.training.dao.SeatDao;
import fa.training.entities.Seat;
import java.util.List;

public class SeatDaoImpl extends GenericDAOImpl<Seat, Integer> implements SeatDao {

    public SeatDaoImpl() {
        super(Seat.class);
    }

    @Override
    protected void initLazyCollections(Seat seat) {
        if (seat != null && seat.getCinemaRoom() != null) {
            seat.getCinemaRoom().getCinemaRoomName(); // force loading
        }
    }

    @Override
    public Seat getSeatByID(int id) {
        return findById(id);
    }

    @Override
    public List<Seat> getAllSeats() {
        return findAll();
    }

    @Override
    public void updateSeatByID(Seat seat) {
        update(seat);
    }

    @Override
    public void deleteSeatById(int id) {
        delete(id);
    }

    @Override
    public void insertSeat(Seat seat) {
        save(seat);
    }
}
