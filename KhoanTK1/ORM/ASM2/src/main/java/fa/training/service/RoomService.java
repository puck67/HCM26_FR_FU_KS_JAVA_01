package fa.training.service;

import fa.training.dao.RoomDAO;
import fa.training.entities.CinemaRoom;
import fa.training.util.Validator;
import java.util.List;
import java.util.Optional;

public class RoomService {

    private final RoomDAO roomDao = new RoomDAO();

    public List<CinemaRoom> getAllRooms() {
        return roomDao.getAll();
    }

    public Optional<CinemaRoom> getRoomById(int id) {
        return roomDao.getById(id);
    }

    public Optional<CinemaRoom> getRoomByName(String name) {
        return roomDao.getByName(name);
    }

    public void addRoom(String name, int seatQuantity) {
        Validator.requireNonBlank(name, "Cinema room name");
        Validator.requireMaxLength(name, 255, "Cinema room name");
        Validator.requirePositive(seatQuantity, "Seat quantity");

        if (roomDao.getByName(name.trim()).isPresent()) {
            throw new IllegalArgumentException("Phong chieu voi ten '" + name + "' da ton tai.");
        }

        CinemaRoom room = new CinemaRoom(name.trim(), seatQuantity);
        roomDao.insert(room);
    }

    public void updateRoom(int id, String name, int seatQuantity) {
        Validator.requireNonBlank(name, "Cinema room name");
        Validator.requireMaxLength(name, 255, "Cinema room name");
        Validator.requirePositive(seatQuantity, "Seat quantity");

        CinemaRoom room = roomDao.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay phong chieu voi ID = " + id));

        // Kiem tra ten trung lap voi phong khac
        Optional<CinemaRoom> existing = roomDao.getByName(name.trim());
        if (existing.isPresent() && existing.get().getCinemaRoomId() != id) {
            throw new IllegalArgumentException("Ten phong chieu '" + name + "' da duoc su dung boi phong khac.");
        }

        room.setCinemaRoomName(name.trim());
        room.setSeatQuantity(seatQuantity);
        roomDao.update(room);
    }

    public void deleteRoom(int id) {
        roomDao.deleteById(id);
    }
}
