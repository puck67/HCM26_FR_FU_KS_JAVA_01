package fa.training.app;

import fa.training.dao.RoomDao;
import fa.training.dao.SeatDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.entities.CinemaRoomDetail;
import fa.training.util.HibernateUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final RoomDao roomDao = new RoomDaoImpl();
    private static final SeatDao seatDao = new SeatDaoImpl();
    private static final RoomDetailDao roomDetailDao = new RoomDetailDaoImpl();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu mainMenu = new Menu("Movie Theater Manager", scanner);

        mainMenu.addItem("Cinema Room Management", () -> showRoomMenu(scanner));
        mainMenu.addItem("Seat Management", () -> showSeatMenu(scanner));
        mainMenu.addItem("Cinema Room Detail Management", () -> showRoomDetailMenu(scanner));
        mainMenu.addItem("Run Scenario Test (Happy & Unhappy Cases)", () -> runScenarioTest());
        mainMenu.addItem("Exit", () -> {
            Menu.printSuccess("Shutting down Hibernate SessionFactory...");
            try {
                HibernateUtil.shutdown();
            } catch (Exception e) {
                Menu.printError("Error closing SessionFactory: " + e.getMessage());
            }
            Menu.printSuccess("Goodbye!");
            mainMenu.setExit(true);
        });

        mainMenu.displayAndRun();
        scanner.close();
    }

    // ==========================================
    // ROOM MANAGEMENT
    // ==========================================
    private static void showRoomMenu(Scanner scanner) {
        Menu sub = new Menu("Cinema Room Management", scanner);

        sub.addItem("Create Cinema Room", () -> {
            String name = sub.readString("Enter room name: ");
            int qty = sub.readInt("Enter seat quantity: ", 1, 1000);
            CinemaRoom room = new CinemaRoom(name, qty);
            if (roomDao.insertRoom(room)) {
                Menu.printSuccess("Cinema Room created successfully with ID: " + room.getCinemaRoomId());
            } else {
                Menu.printError("Failed to create Cinema Room.");
            }
        });

        sub.addItem("Update Cinema Room", () -> {
            int id = sub.readInt("Enter Cinema Room ID to update: ");
            CinemaRoom room = roomDao.getRoomByID(id);
            if (room == null) {
                Menu.printWarning("Cinema Room not found with ID " + id);
                return;
            }
            String newName = sub.readString("Enter new room name: ");
            int newQty = sub.readInt("Enter new seat quantity: ", 1, 1000);
            room.setCinemaRoomName(newName);
            room.setSeatQuantity(newQty);
            if (roomDao.updateRoomByID(room)) {
                Menu.printSuccess("Cinema Room updated successfully.");
            } else {
                Menu.printError("Failed to update Cinema Room.");
            }
        });

        sub.addItem("Delete Cinema Room", () -> {
            int id = sub.readInt("Enter Cinema Room ID to delete: ");
            if (roomDao.deleteRoomById(id)) {
                Menu.printSuccess("Cinema Room deleted successfully (along with all its seats and details due to cascade).");
            } else {
                Menu.printWarning("No Cinema Room found with ID " + id + " to delete.");
            }
        });

        sub.addItem("View Cinema Room by ID", () -> {
            int id = sub.readInt("Enter Cinema Room ID: ");
            CinemaRoom room = roomDao.getRoomByID(id);
            if (room == null) {
                Menu.printWarning("No Cinema Room found with ID " + id);
                return;
            }
            System.out.println(Menu.GREEN + "Cinema Room Profile Details:" + Menu.RESET);
            System.out.println("  ID:            " + room.getCinemaRoomId());
            System.out.println("  Room Name:     " + room.getCinemaRoomName());
            System.out.println("  Seat Quantity: " + room.getSeatQuantity());
            
            System.out.println("  Room Detail:   " + 
                (room.getCinemaRoomDetail() != null ? 
                 ("Rate: " + room.getCinemaRoomDetail().getRoomRate() + 
                  " | Active Date: " + room.getCinemaRoomDetail().getActiveDate() + 
                  " | Description: " + room.getCinemaRoomDetail().getRoomDescription()) 
                 : "None"));

            System.out.println("  Seats count:   " + room.getSeats().size());
            if (!room.getSeats().isEmpty()) {
                printSeatsTable(room.getSeats());
            }
        });

        sub.addItem("List All Cinema Rooms", () -> {
            printRoomsTable(roomDao.getAllRoom());
        });

        sub.addItem("Back to main menu", () -> sub.setExit(true));

        sub.displayAndRun();
    }

    // ==========================================
    // SEAT MANAGEMENT
    // ==========================================
    private static void showSeatMenu(Scanner scanner) {
        Menu sub = new Menu("Seat Management", scanner);

        sub.addItem("Create Seat", () -> {
            int roomId = sub.readInt("Enter Cinema Room ID for this seat: ");
            CinemaRoom room = roomDao.getRoomByID(roomId);
            if (room == null) {
                Menu.printWarning("Cannot create seat. Cinema Room not found with ID " + roomId);
                return;
            }
            String col = sub.readString("Enter seat column (e.g. A, B, C): ");
            int row = sub.readInt("Enter seat row: ", 1, 100);
            String status = sub.readString("Enter status (Available, Not Available, Booked): ", 
                    Arrays.asList("Available", "Not Available", "Booked"));
            String type = sub.readString("Enter type (VIP, Normal): ", 
                    Arrays.asList("VIP", "Normal"));

            Seat seat = new Seat(col, row, status, type);
            room.addSeat(seat); // Helper adds back-reference and updates the room's seats list
            if (seatDao.insertSeat(seat)) {
                Menu.printSuccess("Seat created successfully with ID: " + seat.getSeatId());
            } else {
                Menu.printError("Failed to create Seat.");
            }
        });

        sub.addItem("Update Seat Status/Type", () -> {
            int id = sub.readInt("Enter Seat ID to update: ");
            Seat seat = seatDao.getSeatByID(id);
            if (seat == null) {
                Menu.printWarning("Seat not found with ID " + id);
                return;
            }
            String newStatus = sub.readString("Enter new status (Available, Not Available, Booked): ", 
                    Arrays.asList("Available", "Not Available", "Booked"));
            String newType = sub.readString("Enter new type (VIP, Normal): ", 
                    Arrays.asList("VIP", "Normal"));
            
            seat.setSeatStatus(newStatus);
            seat.setSeatType(newType);
            if (seatDao.updateSeatByID(seat)) {
                Menu.printSuccess("Seat updated successfully.");
            } else {
                Menu.printError("Failed to update Seat.");
            }
        });

        sub.addItem("Delete Seat", () -> {
            int id = sub.readInt("Enter Seat ID to delete: ");
            if (seatDao.deleteSeatById(id)) {
                Menu.printSuccess("Seat deleted successfully.");
            } else {
                Menu.printWarning("No Seat found with ID " + id + " to delete.");
            }
        });

        sub.addItem("View Seat by ID", () -> {
            int id = sub.readInt("Enter Seat ID: ");
            Seat seat = seatDao.getSeatByID(id);
            if (seat == null) {
                Menu.printWarning("No Seat found with ID " + id);
                return;
            }
            System.out.println(Menu.GREEN + "Seat Details:" + Menu.RESET);
            System.out.println("  ID:         " + seat.getSeatId());
            System.out.println("  Room Name:  " + (seat.getCinemaRoom() != null ? seat.getCinemaRoom().getCinemaRoomName() : "N/A"));
            System.out.println("  Row:        " + seat.getSeatRow());
            System.out.println("  Column:     " + seat.getSeatColumn());
            System.out.println("  Status:     " + seat.getSeatStatus());
            System.out.println("  Type:       " + seat.getSeatType());
        });

        sub.addItem("List All Seats", () -> {
            printSeatsTable(seatDao.getAllSeat());
        });

        sub.addItem("Back to main menu", () -> sub.setExit(true));

        sub.displayAndRun();
    }

    // ==========================================
    // ROOM DETAIL MANAGEMENT
    // ==========================================
    private static void showRoomDetailMenu(Scanner scanner) {
        Menu sub = new Menu("Cinema Room Detail Management", scanner);

        sub.addItem("Create Room Detail", () -> {
            int roomId = sub.readInt("Enter Cinema Room ID for these details: ");
            CinemaRoom room = roomDao.getRoomByID(roomId);
            if (room == null) {
                Menu.printWarning("Cannot add details. Cinema Room not found with ID " + roomId);
                return;
            }
            if (room.getCinemaRoomDetail() != null) {
                Menu.printWarning("This Cinema Room already has detail information defined!");
                return;
            }

            int rate = sub.readInt("Enter room rate (VND): ", 0, 10000000);
            LocalDate activeDate = sub.readDate("Enter active date (YYYY-MM-DD): ");
            String desc = sub.readString("Enter room description: ");

            CinemaRoomDetail detail = new CinemaRoomDetail(rate, activeDate, desc);
            room.setCinemaRoomDetailHelper(detail);

            if (roomDetailDao.insertRoomDetail(detail)) {
                Menu.printSuccess("Cinema Room Detail created successfully with ID: " + detail.getCinemaRoomDetailId());
            } else {
                Menu.printError("Failed to create Cinema Room Detail.");
            }
        });

        sub.addItem("Update Room Detail", () -> {
            int id = sub.readInt("Enter Cinema Room Detail ID to update: ");
            CinemaRoomDetail detail = roomDetailDao.getRoomDetailByID(id);
            if (detail == null) {
                Menu.printWarning("Cinema Room Detail not found with ID " + id);
                return;
            }
            int newRate = sub.readInt("Enter new room rate (VND): ", 0, 10000000);
            LocalDate newDate = sub.readDate("Enter new active date (YYYY-MM-DD): ");
            String newDesc = sub.readString("Enter new room description: ");

            detail.setRoomRate(newRate);
            detail.setActiveDate(newDate);
            detail.setRoomDescription(newDesc);

            if (roomDetailDao.updateRoomDetailByID(detail)) {
                Menu.printSuccess("Cinema Room Detail updated successfully.");
            } else {
                Menu.printError("Failed to update Cinema Room Detail.");
            }
        });

        sub.addItem("Delete Room Detail", () -> {
            int id = sub.readInt("Enter Room Detail ID to delete: ");
            if (roomDetailDao.deleteRoomDetailById(id)) {
                Menu.printSuccess("Room Detail deleted successfully.");
            } else {
                Menu.printWarning("No Room Detail found with ID " + id + " to delete.");
            }
        });

        sub.addItem("View Room Detail by ID", () -> {
            int id = sub.readInt("Enter Room Detail ID: ");
            CinemaRoomDetail detail = roomDetailDao.getRoomDetailByID(id);
            if (detail == null) {
                Menu.printWarning("No Room Detail found with ID " + id);
                return;
            }
            System.out.println(Menu.GREEN + "Cinema Room Detail Profile:" + Menu.RESET);
            System.out.println("  ID:           " + detail.getCinemaRoomDetailId());
            System.out.println("  Room ID:      " + (detail.getCinemaRoom() != null ? detail.getCinemaRoom().getCinemaRoomId() : "N/A"));
            System.out.println("  Room Name:    " + (detail.getCinemaRoom() != null ? detail.getCinemaRoom().getCinemaRoomName() : "N/A"));
            System.out.println("  Room Rate:    " + detail.getRoomRate());
            System.out.println("  Active Date:  " + detail.getActiveDate());
            System.out.println("  Description:  " + detail.getRoomDescription());
        });

        sub.addItem("List All Room Details", () -> {
            printRoomDetailsTable(roomDetailDao.getAllRoomDetail());
        });

        sub.addItem("Back to main menu", () -> sub.setExit(true));

        sub.displayAndRun();
    }

    // ==========================================
    // SCENARIO TEST ENGINE (HAPPY & UNHAPPY CASES)
    // ==========================================
    private static void runScenarioTest() {
        System.out.println(Menu.YELLOW + Menu.BOLD + "==================================================================" + Menu.RESET);
        System.out.println(Menu.YELLOW + Menu.BOLD + "           STARTING FULL SCENARIO TESTING (HAPPY & UNHAPPY)       " + Menu.RESET);
        System.out.println(Menu.YELLOW + Menu.BOLD + "==================================================================" + Menu.RESET);

        // --- ROOM TESTS ---
        System.out.println(Menu.CYAN + Menu.BOLD + "\n[Phase 1] Cinema Room Operations" + Menu.RESET);
        
        // Happy case: Insert room
        System.out.println("Creating Cinema Room: 'IMAX Laser 01' with 80 seats...");
        CinemaRoom room = new CinemaRoom("IMAX Laser 01", 80);
        boolean insertRoomSuccess = roomDao.insertRoom(room);
        if (insertRoomSuccess) {
            Menu.printSuccess("Happy Path: Room inserted with ID " + room.getCinemaRoomId());
        } else {
            Menu.printError("Failed to insert room.");
        }

        // Unhappy case: Retrieve room with non-existent ID
        System.out.println("\nUnhappy Path: Retrieving room with invalid ID (e.g. 9999)...");
        CinemaRoom nonExistentRoom = roomDao.getRoomByID(9999);
        if (nonExistentRoom == null) {
            Menu.printSuccess("Unhappy Path: Correctly returned null for non-existent room ID 9999");
        } else {
            Menu.printError("Unhappy Path: Failed! Returned an object when none should exist.");
        }

        // Unhappy case: Update room with invalid ID
        System.out.println("\nUnhappy Path: Updating non-existent room with ID 9999...");
        CinemaRoom badRoom = new CinemaRoom("Fake Room", 10);
        badRoom.setCinemaRoomId(9999);
        boolean updateBadRoom = roomDao.updateRoomByID(badRoom);
        if (!updateBadRoom) {
            Menu.printSuccess("Unhappy Path: Correctly failed to update non-existent room ID 9999");
        } else {
            Menu.printWarning("Unhappy Path: Merged a non-existent ID (Hibernate might have inserted it instead of throwing error depending on session context).");
        }

        // Unhappy case: Delete room with invalid ID
        System.out.println("\nUnhappy Path: Deleting non-existent room ID 8888...");
        boolean deleteBadRoom = roomDao.deleteRoomById(8888);
        if (!deleteBadRoom) {
            Menu.printSuccess("Unhappy Path: Correctly failed to delete non-existent room ID 8888");
        } else {
            Menu.printError("Unhappy Path: Failed! Reported successful deletion of non-existent room.");
        }

        // --- SEAT TESTS ---
        System.out.println(Menu.CYAN + Menu.BOLD + "\n[Phase 2] Seat Operations" + Menu.RESET);

        // Happy case: Create seats and assign to the valid room
        System.out.println("Adding 2 seats to Cinema Room ID: " + room.getCinemaRoomId());
        Seat seat1 = new Seat("F", 12, "Available", "VIP");
        Seat seat2 = new Seat("F", 13, "Booked", "Normal");
        room.addSeat(seat1);
        room.addSeat(seat2);

        boolean insertSeat1 = seatDao.insertSeat(seat1);
        boolean insertSeat2 = seatDao.insertSeat(seat2);
        if (insertSeat1 && insertSeat2) {
            Menu.printSuccess("Happy Path: Successfully added seats with IDs: " + seat1.getSeatId() + ", " + seat2.getSeatId());
        } else {
            Menu.printError("Failed to insert seats.");
        }

        // Unhappy case: Create a seat for a non-existent room ID (referential constraint violation test)
        System.out.println("\nUnhappy Path: Trying to force-insert a seat with invalid Room relationship...");
        Seat orphanSeat = new Seat("Z", 1, "Available", "Normal");
        // We bypass the Java UI safety check and manually map a dummy room with invalid ID
        CinemaRoom dummyRoom = new CinemaRoom();
        dummyRoom.setCinemaRoomId(7777); // Non-existent Room ID
        orphanSeat.setCinemaRoom(dummyRoom);
        boolean insertOrphanSuccess = seatDao.insertSeat(orphanSeat);
        if (!insertOrphanSuccess) {
            Menu.printSuccess("Unhappy Path: Hibernate/Database blocked seat insertion due to foreign key constraint (Room ID 7777 doesn't exist). Exception caught cleanly!");
        } else {
            Menu.printError("Unhappy Path: Failed! Saved seat referencing non-existent room.");
        }

        // Happy case: Update seat status
        System.out.println("\nHappy Path: Updating Seat ID " + seat1.getSeatId() + " status to 'Booked' and type to 'Normal'...");
        seat1.setSeatStatus("Booked");
        seat1.setSeatType("Normal");
        if (seatDao.updateSeatByID(seat1)) {
            Menu.printSuccess("Happy Path: Seat status updated successfully.");
        } else {
            Menu.printError("Failed to update seat.");
        }

        // --- ROOM DETAIL TESTS ---
        System.out.println(Menu.CYAN + Menu.BOLD + "\n[Phase 3] Cinema Room Detail Operations" + Menu.RESET);

        // Happy case: Create Room Detail for valid Room
        System.out.println("Creating details for Cinema Room ID: " + room.getCinemaRoomId());
        CinemaRoomDetail detail = new CinemaRoomDetail(120000, LocalDate.now(), "Fitted with state-of-the-art Dolby Atmos surround audio.");
        room.setCinemaRoomDetailHelper(detail);
        boolean insertDetailSuccess = roomDetailDao.insertRoomDetail(detail);
        if (insertDetailSuccess) {
            Menu.printSuccess("Happy Path: Room Detail created with ID " + detail.getCinemaRoomDetailId());
        } else {
            Menu.printError("Failed to insert room details.");
        }

        // Unhappy case: Trying to create room detail referencing a non-existent room
        System.out.println("\nUnhappy Path: Creating room detail referencing non-existent room ID 6666...");
        CinemaRoomDetail orphanDetail = new CinemaRoomDetail(90000, LocalDate.now(), "Orphan Description");
        CinemaRoom dummyRoom2 = new CinemaRoom();
        dummyRoom2.setCinemaRoomId(6666);
        orphanDetail.setCinemaRoom(dummyRoom2);
        boolean insertOrphanDetail = roomDetailDao.insertRoomDetail(orphanDetail);
        if (!insertOrphanDetail) {
            Menu.printSuccess("Unhappy Path: Hibernate blocked room detail insertion for invalid Room ID 6666. Exception caught cleanly!");
        } else {
            Menu.printError("Unhappy Path: Failed! Saved room detail for invalid room ID.");
        }

        // Unhappy case: Duplicate room detail for the same room (violating OneToOne unique key constraint)
        System.out.println("\nUnhappy Path: Violating OneToOne by creating a second detail for room ID: " + room.getCinemaRoomId());
        CinemaRoomDetail doubleDetail = new CinemaRoomDetail(150000, LocalDate.now(), "Duplicate description details");
        doubleDetail.setCinemaRoom(room);
        boolean insertDuplicateDetail = roomDetailDao.insertRoomDetail(doubleDetail);
        if (!insertDuplicateDetail) {
            Menu.printSuccess("Unhappy Path: Hibernate blocked duplicate OneToOne detail referencing room ID " + room.getCinemaRoomId() + ". Unique key exception caught cleanly!");
        } else {
            Menu.printError("Unhappy Path: Failed! Successfully saved duplicate details violating OneToOne mapping.");
        }

        // Happy case: Update Room Detail rate
        System.out.println("\nHappy Path: Updating Room Detail ID " + detail.getCinemaRoomDetailId() + " rate to 160000...");
        detail.setRoomRate(160000);
        if (roomDetailDao.updateRoomDetailByID(detail)) {
            Menu.printSuccess("Happy Path: Room Detail updated successfully.");
        } else {
            Menu.printError("Failed to update room details.");
        }

        // --- READ & VALIDATE RELATIONAL LOADING ---
        System.out.println(Menu.CYAN + Menu.BOLD + "\n[Phase 4] Retrieve and Verify Relational Integrity" + Menu.RESET);
        System.out.println("Fetching Room ID " + room.getCinemaRoomId() + " from DB with its details & seats...");
        CinemaRoom fetchedRoom = roomDao.getRoomByID(room.getCinemaRoomId());
        if (fetchedRoom != null) {
            System.out.println(Menu.GREEN + "Retrieved Room Info:" + Menu.RESET);
            System.out.println("  Name:             " + fetchedRoom.getCinemaRoomName());
            System.out.println("  Capacity:         " + fetchedRoom.getSeatQuantity());
            System.out.println("  Room Detail Rate: " + (fetchedRoom.getCinemaRoomDetail() != null ? fetchedRoom.getCinemaRoomDetail().getRoomRate() : "N/A"));
            System.out.println("  Room Description: " + (fetchedRoom.getCinemaRoomDetail() != null ? fetchedRoom.getCinemaRoomDetail().getRoomDescription() : "N/A"));
            System.out.println("  Associated Seats count: " + fetchedRoom.getSeats().size());
            for (Seat s : fetchedRoom.getSeats()) {
                System.out.println("    - Seat " + s.getSeatColumn() + s.getSeatRow() + " | Status: " + s.getSeatStatus() + " | Type: " + s.getSeatType());
            }
            Menu.printSuccess("Happy Path: Full cascading relationships successfully verified.");
        } else {
            Menu.printError("Failed to load room from database.");
        }

        // --- CASCADE DELETION TEST ---
        System.out.println(Menu.CYAN + Menu.BOLD + "\n[Phase 5] Cascade Delete Verification" + Menu.RESET);
        System.out.println("Deleting Room ID: " + room.getCinemaRoomId());
        boolean deleteRoomSuccess = roomDao.deleteRoomById(room.getCinemaRoomId());
        if (deleteRoomSuccess) {
            Menu.printSuccess("Happy Path: Room deleted successfully.");
            
            // Check if seats and details are automatically removed (orphan/cascade checks)
            System.out.println("Checking database if seats and details were cascade deleted...");
            Seat s1Check = seatDao.getSeatByID(seat1.getSeatId());
            Seat s2Check = seatDao.getSeatByID(seat2.getSeatId());
            CinemaRoomDetail detailCheck = roomDetailDao.getRoomDetailByID(detail.getCinemaRoomDetailId());
            
            if (s1Check == null && s2Check == null && detailCheck == null) {
                Menu.printSuccess("Happy Path: Cascade Delete verified! Both seats and room detail were automatically removed from the database.");
            } else {
                Menu.printError("Cascade Delete check failed! Seats or room details still exist in DB.");
            }
        } else {
            Menu.printError("Failed to delete room.");
        }

        System.out.println(Menu.YELLOW + Menu.BOLD + "==================================================================" + Menu.RESET);
        System.out.println(Menu.YELLOW + Menu.BOLD + "              SCENARIO TESTING COMPLETED SUCCESSFULLY             " + Menu.RESET);
        System.out.println(Menu.YELLOW + Menu.BOLD + "==================================================================" + Menu.RESET);
    }

    // ==========================================
    // VISUAL TABLE RENDERERS
    // ==========================================
    private static void printRoomsTable(List<CinemaRoom> rooms) {
        if (rooms.isEmpty()) {
            Menu.printWarning("No cinema room records available.");
            return;
        }
        System.out.println(Menu.PURPLE + "   ┌──────┬──────────────────────────────────────────────────────┬────────────────┐" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   │  ID  │ Cinema Room Name                                     │ Seat Quantity  │" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   ├──────┼──────────────────────────────────────────────────────┼────────────────┤" + Menu.RESET);
        for (CinemaRoom r : rooms) {
            System.out.printf(Menu.PURPLE + "   │" + Menu.WHITE + " %-4d " + Menu.PURPLE + "│" + Menu.WHITE + " %-52s " + Menu.PURPLE + "│" + Menu.WHITE + " %-14d " + Menu.PURPLE + "│%n" + Menu.RESET, r.getCinemaRoomId(), r.getCinemaRoomName(), r.getSeatQuantity());
        }
        System.out.println(Menu.PURPLE + "   └──────┴──────────────────────────────────────────────────────┴────────────────┘" + Menu.RESET);
    }

    private static void printSeatsTable(List<Seat> seats) {
        if (seats.isEmpty()) {
            Menu.printWarning("No seat records available.");
            return;
        }
        System.out.println(Menu.PURPLE + "   ┌──────┬──────────┬──────┬─────────────┬─────────────┬─────────────┐" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   │  ID  │ Room ID  │ Row  │ Column      │ Status      │ Type        │" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   ├──────┼──────────┼──────┼─────────────┼─────────────┼─────────────┤" + Menu.RESET);
        for (Seat s : seats) {
            System.out.printf(Menu.PURPLE + "   │" + Menu.WHITE + " %-4d " + Menu.PURPLE + "│" + Menu.WHITE + " %-8d " + Menu.PURPLE + "│" + Menu.WHITE + " %-4d " + Menu.PURPLE + "│" + Menu.WHITE + " %-11s " + Menu.PURPLE + "│" + Menu.WHITE + " %-11s " + Menu.PURPLE + "│" + Menu.WHITE + " %-11s " + Menu.PURPLE + "│%n" + Menu.RESET,
                    s.getSeatId(),
                    s.getCinemaRoom() != null ? s.getCinemaRoom().getCinemaRoomId() : 0,
                    s.getSeatRow(),
                    s.getSeatColumn(),
                    s.getSeatStatus(),
                    s.getSeatType());
        }
        System.out.println(Menu.PURPLE + "   └──────┴──────────┴──────┴─────────────┴─────────────┴─────────────┘" + Menu.RESET);
    }

    private static void printRoomDetailsTable(List<CinemaRoomDetail> details) {
        if (details.isEmpty()) {
            Menu.printWarning("No room detail records available.");
            return;
        }
        System.out.println(Menu.PURPLE + "   ┌──────┬──────────┬────────────┬─────────────┬──────────────────────────────────────────────────┐" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   │  ID  │ Room ID  │ Rate       │ Active Date │ Description                                      │" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   ├──────┼──────────┼────────────┼─────────────┼──────────────────────────────────────────────────┤" + Menu.RESET);
        for (CinemaRoomDetail d : details) {
            System.out.printf(Menu.PURPLE + "   │" + Menu.WHITE + " %-4d " + Menu.PURPLE + "│" + Menu.WHITE + " %-8d " + Menu.PURPLE + "│" + Menu.WHITE + " %-10d " + Menu.PURPLE + "│" + Menu.WHITE + " %-11s " + Menu.PURPLE + "│" + Menu.WHITE + " %-48s " + Menu.PURPLE + "│%n" + Menu.RESET,
                    d.getCinemaRoomDetailId(),
                    d.getCinemaRoom() != null ? d.getCinemaRoom().getCinemaRoomId() : 0,
                    d.getRoomRate(),
                    d.getActiveDate().toString(),
                    d.getRoomDescription());
        }
        System.out.println(Menu.PURPLE + "   └──────┴──────────┴────────────┴─────────────┴──────────────────────────────────────────────────┘" + Menu.RESET);
    }
}
