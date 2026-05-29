package fa.training;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final RoomDao roomDao = new RoomDao();
    private static final RoomDetailDao roomDetailDao = new RoomDetailDao();
    private static final SeatDao seatDao = new SeatDao();

    public static void main(String[] args) {
        // Lambda-based menu: each key maps to a Runnable action
        Map<String, Runnable> menu = new LinkedHashMap<>();
        menu.put("1", Main::handleAddRoom);
        menu.put("2", Main::handleListRooms);
        menu.put("3", Main::handleUpdateRoom);
        menu.put("4", Main::handleDeleteRoom);
        menu.put("5", Main::handleAddRoomDetail);
        menu.put("6", Main::handleListRoomDetails);
        menu.put("7", Main::handleUpdateRoomDetail);
        menu.put("8", Main::handleAddSeat);
        menu.put("9", Main::handleListSeats);
        menu.put("10", Main::handleUpdateSeatStatus);
        menu.put("11", Main::handleDeleteSeat);
        menu.put("12", Main::handleSearchSeatsByStatus);
        menu.put("0", () -> {
            System.out.println("Exiting...");
            HibernateUtil.shutdown();
            System.exit(0);
        });

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            Runnable action = menu.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== MOVIE THEATER MANAGEMENT =====\n");
        sb.append("--- Cinema Room ---\n");
        sb.append("1.  Add Cinema Room\n");
        sb.append("2.  List All Rooms\n");
        sb.append("3.  Update Room\n");
        sb.append("4.  Delete Room\n");
        sb.append("--- Room Detail ---\n");
        sb.append("5.  Add Room Detail\n");
        sb.append("6.  List All Room Details\n");
        sb.append("7.  Update Room Detail\n");
        sb.append("--- Seat ---\n");
        sb.append("8.  Add Seat\n");
        sb.append("9.  List All Seats\n");
        sb.append("10. Update Seat Status/Type\n");
        sb.append("11. Delete Seat\n");
        sb.append("12. Search Seats by Room & Status\n");
        sb.append("0.  Exit\n");
        sb.append("Choose: ");
        System.out.print(sb.toString());
    }

    // --- Cinema Room handlers ---

    private static void handleAddRoom() {
        System.out.print("Room name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine().trim());
        CinemaRoom room = new CinemaRoom(name, capacity);
        roomDao.insertRoom(room);
        System.out.println("Room added: " + room);
    }

    private static void handleListRooms() {
        List<CinemaRoom> rooms = roomDao.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
            return;
        }
        StringBuilder sb = new StringBuilder("All Cinema Rooms:\n");
        rooms.forEach(r -> sb.append("  ").append(r).append("\n"));
        System.out.print(sb.toString());
    }

    private static void handleUpdateRoom() {
        System.out.print("Room ID to update: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        System.out.print("New room name: ");
        String name = scanner.nextLine().trim();
        System.out.print("New capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine().trim());
        boolean updated = roomDao.updateRoomById(id, name, capacity);
        System.out.println(updated ? "Room updated." : "Room not found.");
    }

    private static void handleDeleteRoom() {
        System.out.print("Room ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        boolean deleted = roomDao.deleteRoomById(id);
        System.out.println(deleted ? "Room deleted." : "Room not found.");
    }

    // --- Room Detail handlers ---

    private static void handleAddRoomDetail() {
        System.out.print("Room ID to attach detail: ");
        Long roomId = Long.parseLong(scanner.nextLine().trim());
        CinemaRoom room = roomDao.getRoomById(roomId);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        System.out.print("Room rate: ");
        double rate = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Active date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();

        CinemaRoomDetail detail = new CinemaRoomDetail(rate, date, desc);
        detail.setCinemaRoom(room);
        roomDetailDao.insertRoomDetail(detail);
        System.out.println("Room detail added: " + detail);
    }

    private static void handleListRoomDetails() {
        List<CinemaRoomDetail> details = roomDetailDao.getAllRoomDetails();
        if (details.isEmpty()) {
            System.out.println("No room details found.");
            return;
        }
        StringBuilder sb = new StringBuilder("All Room Details:\n");
        details.forEach(d -> sb.append("  ").append(d).append("\n"));
        System.out.print(sb.toString());
    }

    private static void handleUpdateRoomDetail() {
        System.out.print("Detail ID to update: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        System.out.print("New room rate: ");
        double rate = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("New active date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("New description: ");
        String desc = scanner.nextLine().trim();
        boolean updated = roomDetailDao.updateRoomDetailById(id, rate, date, desc);
        System.out.println(updated ? "Room detail updated." : "Room detail not found.");
    }

    // --- Seat handlers ---

    private static void handleAddSeat() {
        System.out.print("Room ID: ");
        Long roomId = Long.parseLong(scanner.nextLine().trim());
        CinemaRoom room = roomDao.getRoomById(roomId);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        System.out.print("Seat column (e.g. A): ");
        String col = scanner.nextLine().trim();
        System.out.print("Seat row (e.g. 1): ");
        int row = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Seat status (Available/Not Available/Booked): ");
        String status = scanner.nextLine().trim();
        System.out.print("Seat type (VIP/Normal): ");
        String type = scanner.nextLine().trim();

        Seat seat = new Seat(col, row, status, type);
        seat.setCinemaRoom(room);
        seatDao.insertSeat(seat);
        System.out.println("Seat added: " + seat);
    }

    private static void handleListSeats() {
        List<Seat> seats = seatDao.getAllSeats();
        if (seats.isEmpty()) {
            System.out.println("No seats found.");
            return;
        }
        StringBuilder sb = new StringBuilder("All Seats:\n");
        seats.forEach(s -> sb.append("  ").append(s).append("\n"));
        System.out.print(sb.toString());
    }

    private static void handleUpdateSeatStatus() {
        System.out.print("Seat ID to update: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        System.out.print("New status (Available/Not Available/Booked): ");
        String status = scanner.nextLine().trim();
        System.out.print("New type (VIP/Normal): ");
        String type = scanner.nextLine().trim();
        boolean updated = seatDao.updateSeatById(id, status, type);
        System.out.println(updated ? "Seat updated." : "Seat not found.");
    }

    private static void handleDeleteSeat() {
        System.out.print("Seat ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        boolean deleted = seatDao.deleteSeatById(id);
        System.out.println(deleted ? "Seat deleted." : "Seat not found.");
    }

    private static void handleSearchSeatsByStatus() {
        System.out.print("Room ID: ");
        Long roomId = Long.parseLong(scanner.nextLine().trim());
        System.out.print("Status (Available/Not Available/Booked): ");
        String status = scanner.nextLine().trim();
        List<Seat> seats = seatDao.getSeatsByRoomAndStatus(roomId, status);
        if (seats.isEmpty()) {
            System.out.println("No seats found.");
            return;
        }
        StringBuilder sb = new StringBuilder("Seats found:\n");
        seats.forEach(s -> sb.append("  ").append(s).append("\n"));
        System.out.print(sb.toString());
    }
}
