package fa.training;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;

import java.time.LocalDate;
import java.util.List;

public class App {

    public static void main(String[] args) {
        RoomDao roomDao = new RoomDao();
        RoomDetailDao detailDao = new RoomDetailDao();
        SeatDao seatDao = new SeatDao();

        System.out.println("===== SCENARIO: Movie Theater Application =====\n");

        // 1. INSERT rooms
        System.out.println("-- [1] Inserting cinema rooms --");
        CinemaRoom roomA = new CinemaRoom("Room A", 50);
        CinemaRoom roomB = new CinemaRoom("Room B", 30);
        roomDao.insertRoom(roomA);
        roomDao.insertRoom(roomB);
        System.out.println("Inserted: " + roomA);
        System.out.println("Inserted: " + roomB);

        // 2. INSERT room details (OneToOne)
        System.out.println("\n-- [2] Inserting room details --");
        CinemaRoomDetail detailA = new CinemaRoomDetail(150000, LocalDate.of(2024, 1, 10), "Standard room");
        detailA.setCinemaRoom(roomA);
        CinemaRoomDetail detailB = new CinemaRoomDetail(250000, LocalDate.of(2024, 3, 15), "VIP room");
        detailB.setCinemaRoom(roomB);
        detailDao.insertDetail(detailA);
        detailDao.insertDetail(detailB);

        // 3. INSERT seats (OneToMany)
        System.out.println("\n-- [3] Adding seats to Room A --");
        String[] columns = {"A", "B", "C", "D", "E"};
        for (String col : columns) {
            for (int row = 1; row <= 2; row++) {
                String type = col.equals("A") ? "VIP" : "Normal";
                Seat seat = new Seat(col, row, "Available", type);
                seat.setCinemaRoom(roomA);
                seatDao.insertSeat(seat);
            }
        }
        System.out.println("Inserted 10 seats for Room A.");

        // 4. GET ALL & GET BY ID
        System.out.println("\n-- [4] Retrieving rooms --");
        List<CinemaRoom> allRooms = roomDao.getAllRooms();
        allRooms.forEach(System.out::println);

        // 5. BOOK SEATS
        System.out.println("\n-- [5] Booking seats --");
        List<Seat> available = seatDao.getSeatsByStatus("Available");
        System.out.println("Available seats: " + available.size());
        if (available.size() >= 2) {
            seatDao.bookSeat(available.get(0).getSeatId());
            seatDao.bookSeat(available.get(1).getSeatId());
            System.out.println("Booked 2 seats successfully.");
        }

        // 6. UPDATE
        System.out.println("\n-- [6] Updating Room B --");
        roomDao.updateRoomById(roomB.getCinemaRoomId(), "Room B - Upgraded", 40);
        System.out.println("Updated: " + roomDao.getRoomById(roomB.getCinemaRoomId()));

        // 7. QUERY seats by room
        System.out.println("\n-- [7] Seats in Room A --");
        seatDao.getSeatsByRoomId(roomA.getCinemaRoomId()).forEach(System.out::println);

        // 8. DELETE
        System.out.println("\n-- [8] Deleting Room B --");
        roomDao.deleteRoomById(roomB.getCinemaRoomId());
        System.out.println("Rooms remaining: " + roomDao.getAllRooms().size());

        HibernateUtils.shutdown();
        System.out.println("\n===== SCENARIO COMPLETE =====");
    }
}