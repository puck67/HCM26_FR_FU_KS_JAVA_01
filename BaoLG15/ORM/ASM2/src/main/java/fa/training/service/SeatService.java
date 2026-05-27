package fa.training.service;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class SeatService {

    private static final Logger log = LoggerFactory.getLogger(SeatService.class);
    private final SeatDaoImpl seatDao = new SeatDaoImpl();
    private final RoomDaoImpl roomDao = new RoomDaoImpl();

    public List<Seat> getAllSeats() {
        return seatDao.getAll();
    }

    public Optional<Seat> getSeatById(int id) {
        return seatDao.getById(id);
    }

    public List<Seat> getSeatsByRoomId(int roomId) {
        return seatDao.getByRoomId(roomId);
    }

    public List<Seat> getSeatsByStatus(String status) {
        Validator.requireSeatStatus(status);
        return seatDao.getByStatus(status);
    }

    public List<Seat> getSeatsByType(String type) {
        Validator.requireSeatType(type);
        return seatDao.getByType(type);
    }

    public void addSeat(int roomId, String seatColumn, int seatRow, String seatStatus, String seatType) {
        Validator.requireNonBlank(seatColumn, "Seat column");
        Validator.requirePositive(seatRow, "Seat row");
        String validStatus = Validator.requireSeatStatus(seatStatus);
        String validType = Validator.requireSeatType(seatType);

        CinemaRoom room = roomDao.getById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Cinema room with id=" + roomId + " not found."));

        Seat seat = new Seat(room, seatColumn.trim(), seatRow, validStatus, validType);
        seatDao.insert(seat);
        log.info("SeatService: added seat for roomId={}, col='{}', row={}", roomId, seatColumn, seatRow);
    }

    public void updateSeat(int seatId, int roomId, String seatColumn, int seatRow,
                           String seatStatus, String seatType) {
        Validator.requireNonBlank(seatColumn, "Seat column");
        Validator.requirePositive(seatRow, "Seat row");
        String validStatus = Validator.requireSeatStatus(seatStatus);
        String validType = Validator.requireSeatType(seatType);

        Seat seat = seatDao.getById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat with id=" + seatId + " not found."));

        CinemaRoom room = roomDao.getById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Cinema room with id=" + roomId + " not found."));

        seat.setCinemaRoom(room);
        seat.setSeatColumn(seatColumn.trim());
        seat.setSeatRow(seatRow);
        seat.setSeatStatus(validStatus);
        seat.setSeatType(validType);
        seatDao.updateById(seat);
        log.info("SeatService: updated seat id={}", seatId);
    }

    public void deleteSeat(int id) {
        seatDao.deleteById(id);
        log.info("SeatService: deleted seat id={}", id);
    }
}
