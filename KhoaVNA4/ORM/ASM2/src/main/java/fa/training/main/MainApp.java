package fa.training.main;

import fa.training.dao.*;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;
import fa.training.utils.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        RoomDao roomDao = new RoomDaoImpl();
        SeatDao seatDao = new SeatDaoImpl();
        RoomDetailDao detailDao = new RoomDetailDaoImpl();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean keepRunning = true;
            while (keepRunning) {
                printMainMenu();
                int choice = Validator.getValidIntRange(scanner, "Choose an option (0-4): ", 0, 4);

                keepRunning = switch (choice) {
                    case 1 -> {
                        manageCinemaRooms(scanner, roomDao);
                        yield true;
                    }
                    case 2 -> {
                        manageSeats(scanner, seatDao, roomDao);
                        yield true;
                    }
                    case 3 -> {
                        manageRoomDetails(scanner, detailDao, roomDao);
                        yield true;
                    }
                    case 4 -> {
                        simulateScenario(roomDao, seatDao, detailDao);
                        yield true;
                    }
                    case 0 -> {
                        System.out.println("Closing database connections and exiting application...");
                        HibernateUtils.shutdown();
                        System.out.println("Application shut down successfully.");
                        yield false;
                    }
                    default -> {
                        System.out.println("Invalid option. Please try again.");
                        yield true;
                    }
                };
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred in MainApp: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtils.shutdown();
        }
    }

    private static void printMainMenu() {
        System.out.println("\n==================================================");
        System.out.println("       MOVIE THEATER PERSISTENCE MANAGER         ");
        System.out.println("==================================================");
        System.out.println("1. Manage Cinema Rooms");
        System.out.println("2. Manage Seats");
        System.out.println("3. Manage Room Details");
        System.out.println("4. Run Full Scenario Simulation (Auto Demo)");
        System.out.println("0. Exit");
        System.out.println("==================================================");
    }

    private static void manageCinemaRooms(Scanner scanner, RoomDao roomDao) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Manage Cinema Rooms ---");
            System.out.println("1. Add Cinema Room");
            System.out.println("2. View All Cinema Rooms");
            System.out.println("3. Find Cinema Room by ID");
            System.out.println("4. Update Cinema Room");
            System.out.println("5. Delete Cinema Room");
            System.out.println("0. Back to Main Menu");
            int choice = Validator.getValidIntRange(scanner, "Choose option: ", 0, 5);

            switch (choice) {
                case 1 -> {
                    String name = Validator.getNonEmptyString(scanner, "Enter room name: ");
                    int seats = Validator.getValidPositiveInt(scanner, "Enter total seat quantity: ");
                    CinemaRoom room = new CinemaRoom(name, seats);
                    if (roomDao.insertRoom(room)) {
                        System.out.println("Successfully added room: " + room);
                    } else {
                        System.out.println("Failed to add room. (Check if room name already exists)");
                    }
                }
                case 2 -> {
                    List<CinemaRoom> list = roomDao.getAllRoom();
                    if (list.isEmpty()) {
                        System.out.println("No cinema rooms found.");
                    } else {
                        System.out.printf("%-10s %-25s %-15s\n", "ID", "Room Name", "Seat Quantity");
                        System.out.println("--------------------------------------------------");
                        for (CinemaRoom r : list) {
                            System.out.printf("%-10d %-25s %-15d\n", r.getCinemaRoomId(), r.getCinemaRoomName(), r.getSeatQuantity());
                        }
                    }
                }
                case 3 -> {
                    int id = Validator.getValidPositiveInt(scanner, "Enter Room ID to find: ");
                    CinemaRoom room = roomDao.getRoomById(id);
                    if (room != null) {
                        System.out.println("Room found: " + room);
                        if (room.getCinemaRoomDetail() != null) {
                            System.out.println("  Detail: " + room.getCinemaRoomDetail());
                        }
                        if (room.getSeats() != null && !room.getSeats().isEmpty()) {
                            System.out.println("  Seats count: " + room.getSeats().size());
                        }
                    } else {
                        System.out.println("Room not found with ID " + id);
                    }
                }
                case 4 -> {
                    int id = Validator.getValidPositiveInt(scanner, "Enter Room ID to update: ");
                    CinemaRoom room = roomDao.getRoomById(id);
                    if (room != null) {
                        System.out.println("Current details: " + room);
                        String newName = Validator.getNonEmptyString(scanner, "Enter new room name: ");
                        int newSeats = Validator.getValidPositiveInt(scanner, "Enter new seat quantity: ");
                        room.setCinemaRoomName(newName);
                        room.setSeatQuantity(newSeats);
                        if (roomDao.updateRoomById(room)) {
                            System.out.println("Room updated successfully.");
                        } else {
                            System.out.println("Failed to update room.");
                        }
                    } else {
                        System.out.println("Room not found with ID " + id);
                    }
                }
                case 5 -> {
                    int id = Validator.getValidPositiveInt(scanner, "Enter Room ID to delete: ");
                    if (roomDao.deleteRoomById(id)) {
                        System.out.println("Room and all associated seats/details deleted successfully.");
                    } else {
                        System.out.println("Failed to delete room. Check if ID exists.");
                    }
                }
                case 0 -> back = true;
            }
        }
    }

    private static void manageSeats(Scanner scanner, SeatDao seatDao, RoomDao roomDao) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Manage Seats ---");
            System.out.println("1. Add Seat to a Room");
            System.out.println("2. View All Seats");
            System.out.println("3. Update Seat Status");
            System.out.println("4. Delete Seat");
            System.out.println("0. Back to Main Menu");
            int choice = Validator.getValidIntRange(scanner, "Choose option: ", 0, 4);

            switch (choice) {
                case 1 -> {
                    int roomId = Validator.getValidPositiveInt(scanner, "Enter Room ID: ");
                    CinemaRoom room = roomDao.getRoomById(roomId);
                    if (room == null) {
                        System.out.println("Error: Room not found with ID " + roomId);
                        break;
                    }
                    if (room.getSeats().size() >= room.getSeatQuantity()) {
                        System.out.printf("Error: Cannot add seat. Room has reached its maximum capacity of %d seats.\n", room.getSeatQuantity());
                        break;
                    }
                    int row = Validator.getValidPositiveInt(scanner, "Enter Seat Row (positive integer): ");
                    String col = Validator.getNonEmptyString(scanner, "Enter Seat Column (e.g. A, B, C): ");
                    
                    boolean duplicateSeat = room.getSeats().stream()
                            .anyMatch(s -> s.getSeatRow() == row && s.getSeatColumn().equalsIgnoreCase(col));
                    if (duplicateSeat) {
                        System.out.printf("Error: Seat at Row %d, Column '%s' already exists in Room %d.\n", row, col, roomId);
                        break;
                    }

                    String status = Validator.getValidSeatStatus(scanner);
                    String type = Validator.getValidSeatType(scanner);

                    Seat seat = new Seat(col, row, status, type);
                    room.addSeat(seat);

                    if (seatDao.insertSeat(seat)) {
                        System.out.println("Successfully added seat: " + seat);
                    } else {
                        System.out.println("Failed to add seat.");
                    }
                }
                case 2 -> {
                    List<Seat> list = seatDao.getAllSeat();
                    if (list.isEmpty()) {
                        System.out.println("No seats found in database.");
                    } else {
                        System.out.printf("%-10s %-12s %-12s %-15s %-12s %-15s\n", "Seat ID", "Row", "Column", "Status", "Type", "Room ID");
                        System.out.println("--------------------------------------------------------------------------------");
                        for (Seat s : list) {
                            System.out.printf("%-10d %-12d %-12s %-15s %-12s %-15d\n",
                                    s.getSeatId(), s.getSeatRow(), s.getSeatColumn(), s.getSeatStatus(), s.getSeatType(),
                                    s.getCinemaRoom() != null ? s.getCinemaRoom().getCinemaRoomId() : 0);
                        }
                    }
                }
                case 3 -> {
                    int id = Validator.getValidPositiveInt(scanner, "Enter Seat ID to update status: ");
                    Seat seat = seatDao.getSeatById(id);
                    if (seat != null) {
                        System.out.println("Current seat details: " + seat);
                        String newStatus = Validator.getValidSeatStatus(scanner);
                        seat.setSeatStatus(newStatus);
                        if (seatDao.updateSeatById(seat)) {
                            System.out.println("Seat status updated successfully.");
                        } else {
                            System.out.println("Failed to update seat status.");
                        }
                    } else {
                        System.out.println("Seat not found with ID " + id);
                    }
                }
                case 4 -> {
                    int id = Validator.getValidPositiveInt(scanner, "Enter Seat ID to delete: ");
                    Seat seat = seatDao.getSeatById(id);
                    if (seat != null) {
                        CinemaRoom room = seat.getCinemaRoom();
                        if (room != null) {
                            room.removeSeat(seat);
                        }
                        if (seatDao.deleteSeatById(id)) {
                            System.out.println("Seat deleted successfully.");
                        } else {
                            System.out.println("Failed to delete seat.");
                        }
                    } else {
                        System.out.println("Seat not found with ID " + id);
                    }
                }
                case 0 -> back = true;
            }
        }
    }

    private static void manageRoomDetails(Scanner scanner, RoomDetailDao detailDao, RoomDao roomDao) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Manage Room Details ---");
            System.out.println("1. Link Details to a Room");
            System.out.println("2. View All Room Details");
            System.out.println("3. Update Room Details");
            System.out.println("0. Back to Main Menu");
            int choice = Validator.getValidIntRange(scanner, "Choose option: ", 0, 3);

            switch (choice) {
                case 1 -> {
                    int roomId = Validator.getValidPositiveInt(scanner, "Enter Room ID: ");
                    CinemaRoom room = roomDao.getRoomById(roomId);
                    if (room == null) {
                        System.out.println("Error: Room not found with ID " + roomId);
                        break;
                    }
                    if (room.getCinemaRoomDetail() != null) {
                        System.out.println("Error: Room " + roomId + " already has details linked: " + room.getCinemaRoomDetail());
                        break;
                    }
                    int rate = Validator.getValidPositiveInt(scanner, "Enter room rate: ");
                    LocalDate date = Validator.getValidDate(scanner, "Enter active date");
                    String desc = Validator.getNonEmptyString(scanner, "Enter room description: ");

                    CinemaRoomDetail detail = new CinemaRoomDetail(rate, date, desc);
                    room.setCinemaRoomDetail(detail);

                    if (detailDao.insertRoomDetail(detail)) {
                        System.out.println("Successfully linked details to room: " + detail);
                    } else {
                        System.out.println("Failed to save room details.");
                    }
                }
                case 2 -> {
                    List<CinemaRoomDetail> list = detailDao.getAllRoomDetail();
                    if (list.isEmpty()) {
                        System.out.println("No room details found in database.");
                    } else {
                        System.out.printf("%-12s %-15s %-15s %-20s %-30s\n", "Detail ID", "Room ID", "Rate", "Active Date", "Description");
                        System.out.println("----------------------------------------------------------------------------------------------");
                        for (CinemaRoomDetail d : list) {
                            System.out.printf("%-12d %-15d %-15d %-15s %-30s\n",
                                    d.getCinemaRoomDetailId(),
                                    d.getCinemaRoom() != null ? d.getCinemaRoom().getCinemaRoomId() : 0,
                                    d.getRoomRate(),
                                    d.getActiveDate(),
                                    d.getRoomDescription());
                        }
                    }
                }
                case 3 -> {
                    int id = Validator.getValidPositiveInt(scanner, "Enter Room Detail ID to update: ");
                    CinemaRoomDetail detail = detailDao.getRoomDetailById(id);
                    if (detail != null) {
                        System.out.println("Current details: " + detail);
                        int rate = Validator.getValidPositiveInt(scanner, "Enter new room rate: ");
                        LocalDate date = Validator.getValidDate(scanner, "Enter new active date");
                        String desc = Validator.getNonEmptyString(scanner, "Enter new room description: ");

                        detail.setRoomRate(rate);
                        detail.setActiveDate(date);
                        detail.setRoomDescription(desc);

                        if (detailDao.updateRoomDetailById(detail)) {
                            System.out.println("Room details updated successfully.");
                        } else {
                            System.out.println("Failed to update room details.");
                        }
                    } else {
                        System.out.println("Room Detail not found with ID " + id);
                    }
                }
                case 0 -> back = true;
            }
        }
    }

    private static void simulateScenario(RoomDao roomDao, SeatDao seatDao, RoomDetailDao detailDao) {
        System.out.println("\n>>> SIMULATING FULL MOVIE THEATER SCENARIO <<<\n");

        String simRoomName = "Simulated Room 101";
        System.out.println("[Step 1] Checking if old simulation data exists...");
        List<CinemaRoom> existingRooms = roomDao.getAllRoom();
        for (CinemaRoom r : existingRooms) {
            if (simRoomName.equals(r.getCinemaRoomName())) {
                System.out.println("Found existing '" + simRoomName + "'. Deleting to perform clean simulation...");
                roomDao.deleteRoomById(r.getCinemaRoomId());
                System.out.println("Old simulation data deleted via Cascade.");
                break;
            }
        }

        System.out.println("\n[Step 2] Creating CinemaRoom...");
        CinemaRoom room = new CinemaRoom(simRoomName, 10);
        boolean roomInserted = roomDao.insertRoom(room);
        if (!roomInserted) {
            System.err.println("Simulation aborted: Failed to insert CinemaRoom.");
            return;
        }
        System.out.println("Room created successfully: ID = " + room.getCinemaRoomId());

        System.out.println("\n[Step 3] Creating and linking CinemaRoomDetail...");
        CinemaRoomDetail detail = new CinemaRoomDetail(120000, LocalDate.now(), "Premium IMAX Lounge with Dolby 7.1 Sound");
        room.setCinemaRoomDetail(detail);
        boolean detailInserted = detailDao.insertRoomDetail(detail);
        if (!detailInserted) {
            System.err.println("Simulation warning: Failed to insert Room Detail.");
        } else {
            System.out.println("RoomDetail created and linked: ID = " + detail.getCinemaRoomDetailId());
        }

        System.out.println("\n[Step 4] Creating and adding 10 seats...");
        String[] columns = {"A", "B", "C", "D", "E"};
        
        for (int i = 0; i < 5; i++) {
            String status = (i == 2 || i == 4) ? "Booked" : "Available";
            Seat vipSeat = new Seat(columns[i], 1, status, "VIP");
            room.addSeat(vipSeat);
            seatDao.insertSeat(vipSeat);
        }
        
        for (int i = 0; i < 5; i++) {
            String status = (i == 1) ? "Not Available" : "Available";
            Seat normalSeat = new Seat(columns[i], 2, status, "Normal");
            room.addSeat(normalSeat);
            seatDao.insertSeat(normalSeat);
        }
        System.out.println("10 seats generated and successfully linked to Room.");

        System.out.println("\n[Step 5] Retrieving data from DB to verify persistence:");
        CinemaRoom fetchedRoom = roomDao.getRoomById(room.getCinemaRoomId());
        System.out.println("\n==========================================================================");
        System.out.println("                   CINEMA PERSISTENCE REPORT                              ");
        System.out.println("==========================================================================");
        System.out.println("ROOM INFORMATION:");
        System.out.println("  ID:           " + fetchedRoom.getCinemaRoomId());
        System.out.println("  Name:         " + fetchedRoom.getCinemaRoomName());
        System.out.println("  Total Seats:  " + fetchedRoom.getSeatQuantity());
        
        System.out.println("\nROOM DETAILS:");
        CinemaRoomDetail fetchedDetail = fetchedRoom.getCinemaRoomDetail();
        if (fetchedDetail != null) {
            System.out.println("  Detail ID:    " + fetchedDetail.getCinemaRoomDetailId());
            System.out.println("  Rate:         " + fetchedDetail.getRoomRate() + " VND");
            System.out.println("  Active Date:  " + fetchedDetail.getActiveDate());
            System.out.println("  Description:  " + fetchedDetail.getRoomDescription());
        } else {
            System.out.println("  No details found.");
        }

        System.out.println("\nSEAT LAYOUT & STATUS:");
        System.out.printf("  %-10s %-8s %-8s %-15s %-10s\n", "Seat ID", "Row", "Column", "Type", "Status");
        System.out.println("  ----------------------------------------------------------------");
        List<Seat> fetchedSeats = fetchedRoom.getSeats();
        for (Seat s : fetchedSeats) {
            System.out.printf("  %-10d %-8d %-8s %-15s %-10s\n", 
                    s.getSeatId(), s.getSeatRow(), s.getSeatColumn(), s.getSeatType(), s.getSeatStatus());
        }
        System.out.println("==========================================================================\n");
    }
}
