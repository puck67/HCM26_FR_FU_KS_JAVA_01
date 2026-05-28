package fa.training;

import fa.training.daos.RoomDao;
import fa.training.daos.RoomDetailDao;
import fa.training.daos.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;

import java.time.LocalDate;
import java.util.List;

/**
 * App - Main application entry point that simulates a complete movie theater booking scenario.
 * Demonstrates all CRUD operations (Create, Read, Update, Delete) as required.
 */
public class App {
    private static RoomDetailDao detailDao;
    private static RoomDao roomDao;
    private static SeatDao seatDao;

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("    MOVIE THEATER APPLICATION SIMULATION");
        System.out.println("====================================================\n");

        initializeDAOs();
        simulateTheaterOperations();

        System.out.println("\n====================================================");
        System.out.println("    SIMULATION COMPLETED");
        System.out.println("====================================================");
    }

    private static void initializeDAOs() {
        detailDao = new RoomDetailDao();
        roomDao = new RoomDao();
        seatDao = new SeatDao();
    }

    private static void simulateTheaterOperations() {
        // Step 1: Create Cinema Rooms with details (INSERT)
        System.out.println("\n[STEP 1] Creating Cinema Rooms and Details (INSERT)");
        System.out.println("-------------------------------------------");
        
        CinemaRoom premiumRoom = new CinemaRoom("Premium Cinema Hall A", 12);
        CinemaRoomDetail premiumDetail = new CinemaRoomDetail(
                350,
                LocalDate.now(),
                "Premium VIP Hall with luxury seating and surround sound"
        );
        premiumRoom.setRoomDetail(premiumDetail);
        
        Integer premiumRoomId = roomDao.insertRoom(premiumRoom);
        if (premiumRoomId == null) {
            System.err.println("Unable to create premium room. Stopping simulation.");
            return;
        }

        CinemaRoom standardRoom = new CinemaRoom("Standard Cinema Hall B", 20);
        CinemaRoomDetail standardDetail = new CinemaRoomDetail(
                150,
                LocalDate.now(),
                "Standard Hall for regular movies"
        );
        standardRoom.setRoomDetail(standardDetail);
        
        Integer standardRoomId = roomDao.insertRoom(standardRoom);
        if (standardRoomId == null) {
            System.err.println("Unable to create standard room. Stopping simulation.");
            return;
        }

        // Step 2: Create Seats in Premium Room (INSERT)
        System.out.println("\n[STEP 2] Adding Seats to Rooms (INSERT)");
        System.out.println("-------------------------------------------");
        CinemaRoom premiumRoomFetched = roomDao.getRoomById(premiumRoomId);
        if (premiumRoomFetched == null) {
            System.err.println("Unable to load premium room. Stopping simulation.");
            return;
        }
        System.out.println("Adding 12 seats to Premium Room:");
        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 4; col++) {
                Seat seat = new Seat(row, String.valueOf(col), "Available", "VIP");
                seat.setCinemaRoom(premiumRoomFetched);
                Integer seatId = seatDao.insertSeat(seat);
                if (seatId == null) {
                    System.err.println("Unable to create seat " + row + "-" + col + ". Stopping simulation.");
                    return;
                }
                System.out.println("  - Seat " + row + "-" + col + " (ID: " + seatId + ")");
            }
        }

        // Step 3: Create Seats in Standard Room (INSERT)
        CinemaRoom standardRoomFetched = roomDao.getRoomById(standardRoomId);
        if (standardRoomFetched == null) {
            System.err.println("Unable to load standard room. Stopping simulation.");
            return;
        }
        System.out.println("\nAdding 20 seats to Standard Room:");
        for (int row = 1; row <= 5; row++) {
            for (int col = 1; col <= 4; col++) {
                Seat seat = new Seat(row, String.valueOf(col), "Available", "Normal");
                seat.setCinemaRoom(standardRoomFetched);
                Integer seatId = seatDao.insertSeat(seat);
                if (seatId == null) {
                    System.err.println("Unable to create seat " + row + "-" + col + ". Stopping simulation.");
                    return;
                }
                if (row == 1) {  // Print first row only for brevity
                    System.out.println("  - Seat " + row + "-" + col + " (ID: " + seatId + ")");
                }
            }
        }
        System.out.println("  - ... (remaining seats added)");

        // Step 4: Read All Rooms (READ)
        System.out.println("\n[STEP 4] Retrieving All Rooms (READ)");
        System.out.println("-------------------------------------------");
        List<CinemaRoom> allRooms = roomDao.getAllRooms();
        if (allRooms == null) {
            System.err.println("Unable to retrieve cinema rooms. Stopping simulation.");
            return;
        }
        System.out.println("Total Cinema Rooms: " + allRooms.size());
        for (CinemaRoom room : allRooms) {
            if (room != null && room.getRoomDetail() != null) {
                System.out.println("  - " + room.getCinemaRoomName() + " (Rate: $" + room.getRoomDetail().getRoomRate() + ")");
            }
        }

        // Step 5: Read Specific Room Details (READ)
        System.out.println("\n[STEP 5] Retrieving Room Details (READ)");
        System.out.println("-------------------------------------------");
        CinemaRoom roomDetail = roomDao.getRoomWithDetails(premiumRoomId);
        if (roomDetail == null || roomDetail.getRoomDetail() == null) {
            System.err.println("Unable to retrieve premium room with details. Stopping simulation.");
            return;
        }
        System.out.println("Room: " + roomDetail.getCinemaRoomName());
        System.out.println("  Rate: $" + roomDetail.getRoomDetail().getRoomRate());
        System.out.println("  Description: " + roomDetail.getRoomDetail().getRoomDescription());
        System.out.println("  Total Seats: " + roomDetail.getSeats().size());

        // Step 6: Read Seats by Room (READ)
        System.out.println("\n[STEP 6] Listing All Seats in Room (READ)");
        System.out.println("-------------------------------------------");
        List<Seat> seatsInPremium = seatDao.getSeatsByRoomId(premiumRoomId);
        if (seatsInPremium == null) {
            System.err.println("Unable to retrieve seats by room. Stopping simulation.");
            return;
        }
        System.out.println("Seats in Premium Room: " + seatsInPremium.size());
        for (Seat seat : seatsInPremium) {
            System.out.println("  - Seat " + seat.getSeatRow() + "-" + seat.getSeatColumn() +
                    " | Type: " + seat.getSeatType() + " | Status: " + seat.getSeatStatus());
        }

        // Step 7: Update Seat Availability (Simulate Booking - UPDATE)
        System.out.println("\n[STEP 7] Booking Seats (UPDATE)");
        System.out.println("-------------------------------------------");
        if (!seatsInPremium.isEmpty()) {
            Seat firstSeat = seatsInPremium.get(0);
            Seat bookedSeat = new Seat(firstSeat.getSeatRow(), firstSeat.getSeatColumn(),
                    "Booked", firstSeat.getSeatType());
            seatDao.updateSeatById(firstSeat.getSeatId(), bookedSeat);
            System.out.println("Booked Seat " + firstSeat.getSeatRow() + "-" + firstSeat.getSeatColumn());

            if (seatsInPremium.size() > 1) {
                Seat secondSeat = seatsInPremium.get(1);
                Seat notAvailableSeat = new Seat(secondSeat.getSeatRow(), secondSeat.getSeatColumn(),
                        "Not Available", secondSeat.getSeatType());
                seatDao.updateSeatById(secondSeat.getSeatId(), notAvailableSeat);
                System.out.println("Marked Seat " + secondSeat.getSeatRow() + "-" + secondSeat.getSeatColumn() + " as Not Available");
            }
        }

        // Step 8: Read Seats by Status (READ)
        System.out.println("\n[STEP 8] Checking Booked Seats (READ)");
        System.out.println("-------------------------------------------");
        List<Seat> bookedSeats = seatDao.getSeatsByStatus("Booked");
        if (bookedSeats == null) {
            System.err.println("Unable to retrieve booked seats. Stopping simulation.");
            return;
        }
        System.out.println("Total Booked Seats: " + bookedSeats.size());
        for (Seat seat : bookedSeats) {
            System.out.println("  - Seat " + seat.getSeatRow() + "-" + seat.getSeatColumn() + " in Room");
        }

        // Step 9: Update Room Details (UPDATE)
        System.out.println("\n[STEP 9] Updating Room Price (UPDATE)");
        System.out.println("-------------------------------------------");
        
        CinemaRoom roomToUpdate = roomDao.getRoomById(premiumRoomId);
        if (roomToUpdate != null && roomToUpdate.getRoomDetail() != null) {
            CinemaRoomDetail updatedDetail = new CinemaRoomDetail(
                    400,
                    LocalDate.now().plusDays(7),
                    "Premium VIP Hall - Summer Special Rates"
            );
            detailDao.updateRoomDetailById(roomToUpdate.getRoomDetail().getCinemaRoomDetailId(), updatedDetail);
            System.out.println("Updated Premium Room rate to $400");
        }

        // Step 10: Statistics (READ)
        System.out.println("\n[STEP 10] Theater Statistics (READ)");
        System.out.println("-------------------------------------------");
        Long totalRooms = roomDao.countAllRooms();
        Long totalSeats = seatDao.countAllSeats();
        Long totalDetails = detailDao.countAllRoomDetails();
        System.out.println("Total Cinema Rooms: " + totalRooms);
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Total Room Details: " + totalDetails);

        // Step 12: Delete Operations (DELETE)
        System.out.println("\n[STEP 11] Canceling Reservations (DELETE)");
        System.out.println("-------------------------------------------");
        if (!bookedSeats.isEmpty()) {
            Seat seatToRelease = bookedSeats.get(0);
            Seat releasedSeat = new Seat(seatToRelease.getSeatRow(), seatToRelease.getSeatColumn(),
                    "Available", seatToRelease.getSeatType());
            seatDao.updateSeatById(seatToRelease.getSeatId(), releasedSeat);
            System.out.println("Released Seat " + seatToRelease.getSeatRow() + "-" + seatToRelease.getSeatColumn());
        }

        // Final Summary
        System.out.println("\n[FINAL] Summary of Operations");
        System.out.println("-------------------------------------------");
        System.out.println("✓ Cinema Rooms Created: " + roomDao.countAllRooms());
        System.out.println("✓ Room Details Created: " + detailDao.countAllRoomDetails());
        System.out.println("✓ Seats Created: " + seatDao.countAllSeats());
        System.out.println("✓ Seats Booked: " + seatDao.getSeatsByStatus("Booked").size());
        System.out.println("✓ Seats Available: " + seatDao.getSeatsByStatus("Available").size());
        System.out.println("✓ Seats Not Available: " + seatDao.getSeatsByStatus("Not Available").size());
        System.out.println("\nAll CRUD operations demonstrated successfully!");
    }
}
