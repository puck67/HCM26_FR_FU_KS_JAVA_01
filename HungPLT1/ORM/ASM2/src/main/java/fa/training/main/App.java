package fa.training.main;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtil;
import java.time.LocalDate;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("=== MOVIE THEATER APPLICATION SIMULATION ===");

        RoomDao roomDao = new RoomDaoImpl();
        RoomDetailDao detailDao = new RoomDetailDaoImpl();
        SeatDao seatDao = new SeatDaoImpl();

        // 1. Insert Cinema Room, Room Detail and Seats (Simulation Scenario)
        System.out.println("\n--- 1. Creating and Inserting Cinema Room, Detail, and Seats ---");
        CinemaRoom room = new CinemaRoom("Premium Room 1", 3);
        
        CinemaRoomDetail detail = new CinemaRoomDetail(120000, LocalDate.now(), "Premium IMAX Room with Dolby Atmos Sound System");
        room.setCinemaRoomDetail(detail);

        Seat seat1 = new Seat("A", 1, "Available", "VIP");
        Seat seat2 = new Seat("A", 2, "Available", "VIP");
        Seat seat3 = new Seat("B", 1, "Booked", "Normal");

        room.addSeat(seat1);
        room.addSeat(seat2);
        room.addSeat(seat3);

        // Save Room (Cascade will save detail and seats automatically)
        boolean insertSuccess = roomDao.insertRoom(room);
        System.out.println("Insert Room and Cascaded Children Successful: " + insertSuccess);

        // 2. Fetch and Print Records using Stream API & Lambdas
        System.out.println("\n--- 2. Querying Cinema Rooms ---");
        List<CinemaRoom> roomsList = roomDao.getAllRoom();
        roomsList.stream().forEach(r -> {
            System.out.println("Room: " + r);
            if (r.getCinemaRoomDetail() != null) {
                System.out.println("  Detail: " + r.getCinemaRoomDetail());
            }
            System.out.println("  Seats in Room:");
            r.getSeats().stream().forEach(s -> System.out.println("    - " + s));
        });

        // 3. Update Seat status and Room detail
        System.out.println("\n--- 3. Testing Updates (Updating Seat & Room Details) ---");
        CinemaRoom fetchedRoom = roomDao.getRoomById(room.getCinemaRoomId());
        if (fetchedRoom != null) {
            fetchedRoom.setCinemaRoomName("Premium Room 1 (Renovated)");
            
            CinemaRoomDetail fetchedDetail = fetchedRoom.getCinemaRoomDetail();
            if (fetchedDetail != null) {
                fetchedDetail.setRoomRate(150000);
                fetchedDetail.setRoomDescription("Upgraded VIP IMAX Laser Room");
            }

            // Change status of a seat to Booked using stream filter
            fetchedRoom.getSeats().stream()
                    .filter(s -> "A".equals(s.getSeatColumn()) && s.getSeatRow() == 1)
                    .findFirst()
                    .ifPresent(s -> s.setSeatStatus("Booked"));

            boolean updateSuccess = roomDao.updateRoomById(fetchedRoom);
            System.out.println("Update Room Successful: " + updateSuccess);
        }

        // Verify updates
        System.out.println("\n--- 4. Verifying Updated State ---");
        List<CinemaRoom> updatedRooms = roomDao.getAllRoom();
        updatedRooms.stream().forEach(r -> {
            System.out.println("Updated Room: " + r);
            if (r.getCinemaRoomDetail() != null) {
                System.out.println("  Updated Detail: " + r.getCinemaRoomDetail());
            }
            System.out.println("  Updated Seats:");
            r.getSeats().stream().forEach(s -> System.out.println("    - " + s));
        });

        // 5. Test clean delete operations
        System.out.println("\n--- 5. Testing Delete Operation ---");
        // We will insert a temporary room to test clean deletion without deleting the main scenario data
        CinemaRoom tempRoom = new CinemaRoom("Temporary Room", 1);
        CinemaRoomDetail tempDetail = new CinemaRoomDetail(50000, LocalDate.now().minusDays(5), "To be deleted");
        tempRoom.setCinemaRoomDetail(tempDetail);
        tempRoom.addSeat(new Seat("Z", 99, "Not Available", "Normal"));
        
        roomDao.insertRoom(tempRoom);
        System.out.println("Inserted Temporary Room with ID: " + tempRoom.getCinemaRoomId());
        System.out.println("Deleting Temporary Room...");
        boolean deleteSuccess = roomDao.deleteRoomById(tempRoom.getCinemaRoomId());
        System.out.println("Delete Successful: " + deleteSuccess);

        // 6. Clean SessionFactory shutdown
        System.out.println("\nShutting down Hibernate SessionFactory...");
        HibernateUtil.shutdown();
        System.out.println("Simulation complete.");
    }
}
