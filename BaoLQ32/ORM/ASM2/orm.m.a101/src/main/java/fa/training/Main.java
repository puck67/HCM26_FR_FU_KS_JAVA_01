package fa.training;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Entry point.
 *
 * 1. Simulates a full cinema scenario (seed data) on startup.
 * 2. Provides a switch-case menu driven by Lambda (Consumer<Scanner>) actions.
 *
 * UI (Scanner / System.out) is intentionally confined here — never in DAO/Service.
 */
public class Main {

    private static final RoomDao roomDao           = new RoomDao();
    private static final RoomDetailDao detailDao   = new RoomDetailDao();
    private static final SeatDao seatDao           = new SeatDao();

    public static void main(String[] args) {
        System.out.println("=== Movie Theater Application — movietheaterdb (H2) ===\n");

        simulateScenario();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1": listAllRooms.accept(scanner);         break;
                    case "2": findRoomById.accept(scanner);         break;
                    case "3": addRoom.accept(scanner);              break;
                    case "4": updateRoom.accept(scanner);           break;
                    case "5": deleteRoom.accept(scanner);           break;
                    case "6": listAllSeats.accept(scanner);         break;
                    case "7": listSeatsByRoom.accept(scanner);      break;
                    case "8": bookSeat.accept(scanner);             break;
                    case "0": running = false; System.out.println("Goodbye!"); break;
                    default:  System.out.println("Invalid option. Please try again.");
                }
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }

    // ─────────────────────────── Scenario simulation ────────────────────────

    private static void simulateScenario() {
        System.out.println("--- Simulating scenario ---");

        // 1. Create a cinema room
        CinemaRoom room = new CinemaRoom("Hall A", 6);

        // 2. Add detail (OneToOne)
        CinemaRoomDetail detail = new CinemaRoomDetail(
                150_000, LocalDate.of(2024, 1, 15), "Main screening hall");
        room.setDetail(detail);

        // 3. Add seats (OneToMany): columns A-C, rows 1-2
        String[] cols = {"A", "B", "C"};
        for (int row = 1; row <= 2; row++) {
            for (String col : cols) {
                String type = (row == 1 && col.equals("B")) ? "VIP" : "Normal";
                room.addSeat(new Seat(col, row, "Available", type));
            }
        }

        roomDao.saveRoom(room);
        System.out.println("Created: " + room);

        // 4. Book seat A1 → status 'Booked'
        List<Seat> seats = seatDao.findSeatsByRoomId(room.getCinemaRoomId());
        if (!seats.isEmpty()) {
            Seat first = seats.get(0);
            seatDao.updateSeatById(first.getSeatId(), "Booked", first.getSeatType());
            System.out.println("Booked seat: " + first);
        }

        // 5. Update room name
        roomDao.updateRoomById(room.getCinemaRoomId(), "Hall A - Premium", 6);
        System.out.println("Updated room name to 'Hall A - Premium'");

        System.out.println("--- Scenario complete ---\n");
    }

    // ─────────────────────────────── Menu lambdas ───────────────────────────

    private static final Consumer<Scanner> listAllRooms = sc -> {
        List<CinemaRoom> rooms = roomDao.findAllRooms();
        rooms.forEach(System.out::println);
        if (rooms.isEmpty()) System.out.println("No rooms found.");
    };

    private static final Consumer<Scanner> findRoomById = sc -> {
        System.out.print("Room ID: ");
        int id = readInt(sc);
        Optional<CinemaRoom> room = roomDao.findRoomById(id);
        room.ifPresent(r -> System.out.println(r + "\n  Detail: " + r.getCinemaRoomDetail()));
        if (!room.isPresent()) System.out.println("Not found.");
    };

    private static final Consumer<Scanner> addRoom = sc -> {
        System.out.print("Room name: ");
        String name = sc.nextLine().trim();
        System.out.print("Seat quantity: ");
        int qty = readInt(sc);

        CinemaRoom room = new CinemaRoom(name, qty);
        CinemaRoomDetail detail = new CinemaRoomDetail(100_000, LocalDate.now(), "New room");
        room.setDetail(detail);
        roomDao.saveRoom(room);
        System.out.println("Saved: " + room);
    };

    private static final Consumer<Scanner> updateRoom = sc -> {
        System.out.print("Room ID to update: ");
        int id = readInt(sc);
        System.out.print("New name: ");
        String name = sc.nextLine().trim();
        System.out.print("New seat quantity: ");
        int qty = readInt(sc);
        boolean ok = roomDao.updateRoomById(id, name, qty);
        System.out.println(ok ? "Updated." : "Room id=" + id + " not found.");
    };

    private static final Consumer<Scanner> deleteRoom = sc -> {
        System.out.print("Room ID to delete: ");
        int id = readInt(sc);
        boolean ok = roomDao.deleteRoomById(id);
        System.out.println(ok ? "Deleted (cascade to detail + seats)." : "Room id=" + id + " not found.");
    };

    private static final Consumer<Scanner> listAllSeats = sc -> {
        List<Seat> seats = seatDao.findAllSeats();
        seats.forEach(System.out::println);
        if (seats.isEmpty()) System.out.println("No seats found.");
    };

    private static final Consumer<Scanner> listSeatsByRoom = sc -> {
        System.out.print("Room ID: ");
        int id = readInt(sc);
        List<Seat> seats = seatDao.findSeatsByRoomId(id);
        seats.forEach(System.out::println);
        if (seats.isEmpty()) System.out.println("No seats for room id=" + id);
    };

    private static final Consumer<Scanner> bookSeat = sc -> {
        System.out.print("Seat ID to book: ");
        int id = readInt(sc);
        Optional<Seat> seat = seatDao.findSeatById(id);
        if (!seat.isPresent()) {
            System.out.println("Seat not found.");
            return;
        }
        boolean ok = seatDao.updateSeatById(id, "Booked", seat.get().getSeatType());
        System.out.println(ok ? "Seat " + id + " is now Booked." : "Failed.");
    };

    // ─────────────────────────────── Helpers ────────────────────────────────

    private static void printMenu() {
        System.out.println("--- MENU ---");
        System.out.println("1. List all rooms");
        System.out.println("2. Find room by ID");
        System.out.println("3. Add room");
        System.out.println("4. Update room");
        System.out.println("5. Delete room");
        System.out.println("6. List all seats");
        System.out.println("7. List seats by room");
        System.out.println("8. Book a seat");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    /** Defensive int parsing — prevents crash on non-numeric input. */
    private static int readInt(Scanner sc) {
        while (true) {
            String line = sc.nextLine().trim();
            if (line.matches("-?\\d+")) return Integer.parseInt(line);
            System.out.print("Invalid input. Enter a number: ");
        }
    }
}
