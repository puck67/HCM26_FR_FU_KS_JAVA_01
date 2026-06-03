package fa.training;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import java.time.LocalDate;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("=== Starting Movie Theater Application Simulation ===");

        // Instantiate DAOs
        RoomDao roomDao = new RoomDaoImpl();
        RoomDetailDao detailDao = new RoomDetailDaoImpl();
        SeatDao seatDao = new SeatDaoImpl();

        // 1. Create and Save Cinema Rooms
        System.out.println("\n--- Step 1: Creating Cinema Rooms ---");
        CinemaRoom room1 = new CinemaRoom("Room 01 - IMAX", 100);
        CinemaRoom room2 = new CinemaRoom("Room 02 - Standard", 150);

        roomDao.insertRoom(room1);
        roomDao.insertRoom(room2);

        System.out.println("Inserted Rooms:");
        roomDao.getAllRooms().forEach(System.out::println);

        // 2. Create and Save Room Details (One-to-One)
        System.out.println("\n--- Step 2: Adding Room Details ---");
        CinemaRoomDetail detail1 = new CinemaRoomDetail(150000, LocalDate.of(2026, 5, 29), "IMAX Screen with Dolby Atmos");
        CinemaRoomDetail detail2 = new CinemaRoomDetail(80000, LocalDate.of(2026, 5, 29), "Standard cinema room with stereo sound");

        // Associate details with room (and vice-versa)
        detail1.setCinemaRoom(room1);
        detail2.setCinemaRoom(room2);

        detailDao.insertRoomDetail(detail1);
        detailDao.insertRoomDetail(detail2);

        System.out.println("Inserted Room Details:");
        detailDao.getAllRoomDetails().forEach(System.out::println);

        // 3. Create and Save Seats (One-to-Many / Many-to-One)
        System.out.println("\n--- Step 3: Inserting Seats ---");
        Seat seat1 = new Seat("A", 1, "Available", "Normal");
        Seat seat2 = new Seat("A", 2, "Available", "Normal");
        Seat seat3 = new Seat("E", 10, "Available", "VIP");

        // Associate seats with room1
        seat1.setCinemaRoom(room1);
        seat2.setCinemaRoom(room1);
        seat3.setCinemaRoom(room1);

        seatDao.insertSeat(seat1);
        seatDao.insertSeat(seat2);
        seatDao.insertSeat(seat3);

        System.out.println("Inserted Seats:");
        seatDao.getAllSeats().forEach(System.out::println);

        // 4. Read / Find Entity by ID
        System.out.println("\n--- Step 4: Testing 'Get by ID' Methods ---");
        CinemaRoom retrievedRoom = roomDao.getRoomById(room1.getCinemaRoomId());
        System.out.println("Retrieved Room: " + retrievedRoom);

        Seat retrievedSeat = seatDao.getSeatById(seat3.getSeatId());
        System.out.println("Retrieved Seat: " + retrievedSeat);

        CinemaRoomDetail retrievedDetail = detailDao.getRoomDetailById(detail1.getCinemaRoomDetailId());
        System.out.println("Retrieved Room Detail: " + retrievedDetail);

        // 5. Update Operations
        System.out.println("\n--- Step 5: Updating Entities ---");
        // Update Room
        retrievedRoom.setCinemaRoomName("Room 01 - IMAX 3D");
        roomDao.updateRoomById(retrievedRoom);
        System.out.println("Updated Room name: " + roomDao.getRoomById(room1.getCinemaRoomId()));

        // Update Detail
        retrievedDetail.setRoomRate(170000);
        detailDao.updateRoomDetailById(retrievedDetail);
        System.out.println("Updated Room Detail rate: " + detailDao.getRoomDetailById(detail1.getCinemaRoomDetailId()));

        // Update Seat
        retrievedSeat.setSeatStatus("Booked");
        seatDao.updateSeatById(retrievedSeat);
        System.out.println("Updated Seat status: " + seatDao.getSeatById(seat3.getSeatId()));

        // 6. Delete Operations
        System.out.println("\n--- Step 6: Deleting Entities ---");
        // Delete seat2
        System.out.println("Deleting Seat with ID: " + seat2.getSeatId());
        seatDao.deleteSeatById(seat2.getSeatId());
        System.out.println("Remaining Seats after delete:");
        seatDao.getAllSeats().forEach(System.out::println);

        // Delete room2 (this should cascade and delete detail2 because of cascade = CascadeType.ALL)
        System.out.println("Deleting Cinema Room with ID: " + room2.getCinemaRoomId());
        roomDao.deleteRoomById(room2.getCinemaRoomId());

        System.out.println("Remaining Rooms after deleting Room 2:");
        roomDao.getAllRooms().forEach(System.out::println);

        System.out.println("Remaining Details after deleting Room 2 (Cascade check):");
        detailDao.getAllRoomDetails().forEach(System.out::println);

        // Cleanup resources
        HibernateUtil.shutdown();
        System.out.println("\n=== Simulation Finished Successfully ===");
    }
}
