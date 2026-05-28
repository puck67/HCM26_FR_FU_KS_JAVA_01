package fa.training.dao;

import fa.training.entities.Seat;
import java.util.List;

public interface SeatDao {
    boolean insertSeat(Seat seat);
    Seat getSeatByID(int id);
    List<Seat> getAllSeat();
    boolean updateSeatByID(int id, Seat seat);
    boolean deleteSeatById(int id);
}
