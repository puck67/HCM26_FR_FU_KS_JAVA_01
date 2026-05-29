package fa.training.dao;

import fa.training.entities.Seat;
import java.util.List;

public interface SeatDao {
    boolean insertSeat(Seat seat);
    Seat getSeatById(int seatId);
    List<Seat> getAllSeats();
    boolean updateSeatById(Seat seat);
    boolean deleteSeatById(int seatId);
}
