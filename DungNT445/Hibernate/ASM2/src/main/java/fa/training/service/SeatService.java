package fa.training.service;

import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import java.util.List;

public class SeatService {
    private final SeatDao seatDao = new SeatDao();

    public void addSeat(String column, int row, String status, String type, CinemaRoom room) {
        Seat seat = new Seat(column, row, status, type, room);
        seatDao.insertSeat(seat);
    }

    public List<Seat> getAllSeats() {
        return seatDao.getAllSeat();
    }
}
