package fa.training.dao;

import fa.training.entities.Seat;
import java.util.List;

public interface SeatDao extends GenericDAO<Seat, Integer> {
    Seat getSeatByID(int id);
    List<Seat> getAllSeats();
    void updateSeatByID(Seat seat);
    void deleteSeatById(int id);
    void insertSeat(Seat seat);
}
