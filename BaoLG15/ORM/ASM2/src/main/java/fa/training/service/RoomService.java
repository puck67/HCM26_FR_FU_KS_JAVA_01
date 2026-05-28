package fa.training.service;

import fa.training.dao.impl.RoomDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    private final RoomDaoImpl roomDao = new RoomDaoImpl();

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

        roomDao.getByName(name.trim()).ifPresent(r -> {
            throw new IllegalArgumentException("Cinema room name '" + name + "' already exists.");
        });

        CinemaRoom room = new CinemaRoom(name.trim(), seatQuantity);
        roomDao.insert(room);
        log.info("RoomService: added room '{}'", name);
    }

    public void updateRoom(int id, String name, int seatQuantity) {
        Validator.requireNonBlank(name, "Cinema room name");
        Validator.requireMaxLength(name, 255, "Cinema room name");
        Validator.requirePositive(seatQuantity, "Seat quantity");

        CinemaRoom room = roomDao.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cinema room with id=" + id + " not found."));

        room.setCinemaRoomName(name.trim());
        room.setSeatQuantity(seatQuantity);
        roomDao.updateById(room);
        log.info("RoomService: updated room id={}", id);
    }

    public void deleteRoom(int id) {
        roomDao.deleteById(id);
        log.info("RoomService: deleted room id={}", id);
    }
}
