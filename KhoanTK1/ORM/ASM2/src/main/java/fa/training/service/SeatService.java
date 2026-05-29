package fa.training.service;

import fa.training.dao.RoomDAO;
import fa.training.dao.SeatDAO;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.util.Validator;
import java.util.List;
import java.util.Optional;

public class SeatService {

    private final SeatDAO seatDao = new SeatDAO();
    private final RoomDAO roomDao = new RoomDAO();

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
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay phong chieu voi ID = " + roomId));

        // Kiem tra vi tri ghe trung lap trong phong chieu
        List<Seat> roomSeats = seatDao.getByRoomId(roomId);
        for (Seat s : roomSeats) {
            if (s.getSeatRow() == seatRow && s.getSeatColumn().equalsIgnoreCase(seatColumn.trim())) {
                throw new IllegalArgumentException("Ghe tai dong " + seatRow + " cot " + seatColumn + " da ton tai trong phong nay.");
            }
        }

        Seat seat = new Seat(room, seatColumn.trim(), seatRow, validStatus, validType);
        seatDao.insert(seat);
    }

    public void updateSeat(int seatId, int roomId, String seatColumn, int seatRow,
                           String seatStatus, String seatType) {
        Validator.requireNonBlank(seatColumn, "Seat column");
        Validator.requirePositive(seatRow, "Seat row");
        String validStatus = Validator.requireSeatStatus(seatStatus);
        String validType = Validator.requireSeatType(seatType);

        Seat seat = seatDao.getById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay ghe voi ID = " + seatId));

        CinemaRoom room = roomDao.getById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay phong chieu voi ID = " + roomId));

        // Kiem tra vi tri ghe trung lap
        List<Seat> roomSeats = seatDao.getByRoomId(roomId);
        for (Seat s : roomSeats) {
            if (s.getSeatId() != seatId && s.getSeatRow() == seatRow && s.getSeatColumn().equalsIgnoreCase(seatColumn.trim())) {
                throw new IllegalArgumentException("Ghe tai dong " + seatRow + " cot " + seatColumn + " da ton tai o ghe khac.");
            }
        }

        seat.setCinemaRoom(room);
        seat.setSeatColumn(seatColumn.trim());
        seat.setSeatRow(seatRow);
        seat.setSeatStatus(validStatus);
        seat.setSeatType(validType);
        seatDao.update(seat);
    }

    public void deleteSeat(int id) {
        seatDao.deleteById(id);
    }
}
