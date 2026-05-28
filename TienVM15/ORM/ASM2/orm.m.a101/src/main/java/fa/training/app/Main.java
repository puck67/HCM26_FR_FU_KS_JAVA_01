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
import fa.training.util.TableRenderer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    private static final RoomDao roomDao = new RoomDaoImpl();
    private static final SeatDao seatDao = new SeatDaoImpl();
    private static final RoomDetailDao detailDao = new RoomDetailDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);
    private static final MenuManager menu = new MenuManager(scanner);

    public static void main(String[] args) {
        System.out.println("Initializing Database Connection to movietheaterdb...");
        try {
            HibernateUtil.getSessionFactory();
            System.out.println("Database Connection initialized successfully.");
        } catch (Exception e) {
            System.err.println("Database Connection failed. Make sure MySQL server is running and database 'movietheaterdb' exists.");
            e.printStackTrace();
            System.exit(1);
        }

        runMainMenu();
    }

    private static void runMainMenu() {
        Map<Integer, String> descriptions = new LinkedHashMap<>();
        Map<Integer, MenuAction> actions = new LinkedHashMap<>();

        descriptions.put(1, "Manage Cinema Rooms");
        descriptions.put(2, "Manage Seats");
        descriptions.put(3, "Manage Room Details");
        descriptions.put(4, "Run Booking Simulation");
        descriptions.put(0, "Exit App");

        actions.put(1, Main::runRoomMenu);
        actions.put(2, Main::runSeatMenu);
        actions.put(3, Main::runDetailMenu);
        actions.put(4, Main::runSimulation);
        actions.put(0, () -> {
            System.out.println("Closing SessionFactory and exiting. Goodbye!");
            HibernateUtil.shutdown();
            System.exit(0);
        });

        menu.runMenu("MOVIE THEATER APP - MAIN MENU", descriptions, actions, false);
    }

    // -------------------------------------------------------------------------
    // Cinema Room Management Submenu
    // -------------------------------------------------------------------------
    private static void runRoomMenu() {
        Map<Integer, String> descriptions = new LinkedHashMap<>();
        Map<Integer, MenuAction> actions = new LinkedHashMap<>();

        descriptions.put(1, "Create Cinema Room");
        descriptions.put(2, "Update Cinema Room");
        descriptions.put(3, "Delete Cinema Room");
        descriptions.put(4, "List All Cinema Rooms");
        descriptions.put(0, "Back to Main Menu");

        actions.put(1, () -> {
            String name = menu.readNonEmptyString("Enter room name: ");
            int seats = menu.readPositiveInt("Enter seat quantity: ");
            roomDao.insertRoom(new CinemaRoom(name, seats));
            System.out.println("✅ Cinema Room created successfully!");
        });
        actions.put(2, () -> {
            if (!printRoomsSummary()) return;
            int id = menu.readInt("Enter room ID to update: ");
            CinemaRoom room = roomDao.getRoomByID(id);
            if (room != null) {
                String name = menu.readNonEmptyString("Enter new room name: ");
                int seats = menu.readPositiveInt("Enter new seat quantity: ");
                room.setCinemaRoomName(name);
                room.setSeatQuantity(seats);
                roomDao.updateRoomByID(room);
                System.out.println("✅ Cinema Room updated successfully!");
            } else {
                System.out.println("❌ Cinema Room not found.");
            }
        });
        actions.put(3, () -> {
            if (!printRoomsSummary()) return;
            int id = menu.readInt("Enter room ID to delete: ");
            roomDao.deleteRoomById(id);
            System.out.println("✅ Cinema Room deleted successfully!");
        });
        actions.put(4, () -> {
            printRoomsSummary();
        });

        menu.runMenu("CINEMA ROOM MANAGEMENT", descriptions, actions);
    }

    // -------------------------------------------------------------------------
    // Seat Management Submenu
    // -------------------------------------------------------------------------
    private static void runSeatMenu() {
        Map<Integer, String> descriptions = new LinkedHashMap<>();
        Map<Integer, MenuAction> actions = new LinkedHashMap<>();

        descriptions.put(1, "Create Seat");
        descriptions.put(2, "Update Seat Status/Type");
        descriptions.put(3, "Delete Seat");
        descriptions.put(4, "List All Seats");
        descriptions.put(0, "Back to Main Menu");

        actions.put(1, () -> {
            if (!printRoomsSummary()) return;
            int roomId = menu.readInt("Select Cinema Room ID: ");
            CinemaRoom room = roomDao.getRoomByID(roomId);
            if (room != null) {
                String col = menu.readNonEmptyString("Enter seat column (e.g. A, B): ");
                int row = menu.readPositiveInt("Enter seat row: ");
                String status = menu.readStringWithChoices("Enter status (Available/Not Available/Booked): ", List.of("Available", "Not Available", "Booked"));
                String type = menu.readStringWithChoices("Enter type (VIP/Normal): ", List.of("VIP", "Normal"));
                
                Seat seat = new Seat(col, row, status, type);
                room.addSeat(seat);
                roomDao.updateRoomByID(room);
                System.out.println("✅ Seat created and linked to room successfully!");
            } else {
                System.out.println("❌ Cinema Room not found.");
            }
        });
        actions.put(2, () -> {
            if (!printSeatsSummary()) return;
            int id = menu.readInt("Enter Seat ID to update: ");
            Seat seat = seatDao.getSeatByID(id);
            if (seat != null) {
                String status = menu.readStringWithChoices("Enter new status (Available/Not Available/Booked): ", List.of("Available", "Not Available", "Booked"));
                String type = menu.readStringWithChoices("Enter new type (VIP/Normal): ", List.of("VIP", "Normal"));
                seat.setSeatStatus(status);
                seat.setSeatType(type);
                seatDao.updateSeatByID(seat);
                System.out.println("✅ Seat updated successfully!");
            } else {
                System.out.println("❌ Seat not found.");
            }
        });
        actions.put(3, () -> {
            if (!printSeatsSummary()) return;
            int id = menu.readInt("Enter Seat ID to delete: ");
            seatDao.deleteSeatById(id);
            System.out.println("✅ Seat deleted successfully!");
        });
        actions.put(4, () -> {
            printSeatsSummary();
        });

        menu.runMenu("SEAT MANAGEMENT", descriptions, actions);
    }

    // -------------------------------------------------------------------------
    // Room Detail Management Submenu
    // -------------------------------------------------------------------------
    private static void runDetailMenu() {
        Map<Integer, String> descriptions = new LinkedHashMap<>();
        Map<Integer, MenuAction> actions = new LinkedHashMap<>();

        descriptions.put(1, "Link Detail to Cinema Room");
        descriptions.put(2, "Update Room Detail");
        descriptions.put(3, "Delete Room Detail");
        descriptions.put(4, "List All Room Details");
        descriptions.put(0, "Back to Main Menu");

        actions.put(1, () -> {
            if (!printRoomsSummary()) return;
            int roomId = menu.readInt("Select Cinema Room ID: ");
            CinemaRoom room = roomDao.getRoomByID(roomId);
            if (room != null) {
                if (room.getCinemaRoomDetail() != null) {
                    System.out.println("⚠️ Room already has detailed information linked.");
                    return;
                }
                int rate = menu.readNonNegativeInt("Enter room rate (VND): ");
                LocalDate activeDate = menu.readLocalDate("Enter active date (yyyy-MM-dd, empty for today): ", true);
                String desc = menu.readStringWithLengthLimit("Enter room description (max 250 chars): ", 250);

                CinemaRoomDetail detail = new CinemaRoomDetail(rate, activeDate, desc);
                room.setCinemaRoomDetail(detail);
                roomDao.updateRoomByID(room);
                System.out.println("✅ Room details linked successfully!");
            } else {
                System.out.println("❌ Cinema Room not found.");
            }
        });
        actions.put(2, () -> {
            if (!printDetailsSummary()) return;
            int id = menu.readInt("Enter Detail ID to update: ");
            CinemaRoomDetail detail = detailDao.getRoomDetailByID(id);
            if (detail != null) {
                int rate = menu.readNonNegativeInt("Enter new room rate (VND): ");
                String desc = menu.readStringWithLengthLimit("Enter new description (max 250 chars): ", 250);
                detail.setRoomRate(rate);
                detail.setRoomDescription(desc);
                detailDao.updateRoomDetailByID(detail);
                System.out.println("✅ Room detail updated successfully!");
            } else {
                System.out.println("❌ Room detail not found.");
            }
        });
        actions.put(3, () -> {
            if (!printDetailsSummary()) return;
            int id = menu.readInt("Enter Detail ID to delete: ");
            detailDao.deleteRoomDetailById(id);
            System.out.println("✅ Room detail deleted successfully!");
        });
        actions.put(4, () -> {
            printDetailsSummary();
        });

        menu.runMenu("ROOM DETAIL MANAGEMENT", descriptions, actions);
    }

    // -------------------------------------------------------------------------
    // Simulation
    // -------------------------------------------------------------------------
    private static void runSimulation() {
        System.out.println("\n🎬 Starting Booking Simulation Scenario...");

        // Create Cinema Room
        String roomName = "IMAX Laser Room " + (new Random().nextInt(100) + 1);
        CinemaRoom room = new CinemaRoom(roomName, 5);
        roomDao.insertRoom(room);
        System.out.println("1. Created Cinema Room: " + roomName);

        // Add 5 seats to room
        room.addSeat(new Seat("A", 1, "Available", "Normal"));
        room.addSeat(new Seat("A", 2, "Available", "Normal"));
        room.addSeat(new Seat("B", 1, "Booked", "VIP"));
        room.addSeat(new Seat("B", 2, "Booked", "VIP"));
        room.addSeat(new Seat("C", 1, "Not Available", "Normal"));
        roomDao.updateRoomByID(room);
        System.out.println("2. Inserted 5 seats (VIP / Normal / Booked / Available)");

        // Add detailed room info
        CinemaRoomDetail detail = new CinemaRoomDetail(180000, LocalDate.now(), "Top-tier Laser IMAX with Dolby Atmos");
        room.setCinemaRoomDetail(detail);
        roomDao.updateRoomByID(room);
        System.out.println("3. Linked detailed rate and active date to the room");

        System.out.println("\n🎉 Simulation complete! Here is the data inside movietheaterdb:");
        
        displayRooms("Registered Rooms", List.of(room));
        displaySeats("Linked Seats", new ArrayList<>(room.getSeats()));
        displayDetails("Linked Room Details", List.of(room.getCinemaRoomDetail()));
    }

    // -------------------------------------------------------------------------
    // Printers & Table Rendering Helpers
    // -------------------------------------------------------------------------
    private static void displayRooms(String title, List<CinemaRoom> list) {
        List<String> headers = List.of("Room ID", "Room Name", "Seat Capacity", "Has Details");
        List<List<String>> rows = new ArrayList<>();
        for (CinemaRoom r : list) {
            rows.add(List.of(
                String.valueOf(r.getCinemaRoomId()),
                r.getCinemaRoomName(),
                String.valueOf(r.getSeatQuantity()),
                r.getCinemaRoomDetail() != null ? "Yes" : "No"
            ));
        }
        TableRenderer.printTable(title, headers, rows);
    }

    private static void displaySeats(String title, List<Seat> list) {
        List<String> headers = List.of("Seat ID", "Room Name", "Col", "Row", "Status", "Type");
        List<List<String>> rows = new ArrayList<>();
        for (Seat s : list) {
            rows.add(List.of(
                String.valueOf(s.getSeatId()),
                s.getCinemaRoom() != null ? s.getCinemaRoom().getCinemaRoomName() : "N/A",
                s.getSeatColumn(),
                String.valueOf(s.getSeatRow()),
                s.getSeatStatus(),
                s.getSeatType()
            ));
        }
        TableRenderer.printTable(title, headers, rows);
    }

    private static void displayDetails(String title, List<CinemaRoomDetail> list) {
        List<String> headers = List.of("Detail ID", "Room Name", "Rate (VND)", "Active Date", "Description");
        List<List<String>> rows = new ArrayList<>();
        for (CinemaRoomDetail d : list) {
            if (d == null) continue;
            rows.add(List.of(
                String.valueOf(d.getCinemaRoomDetailId()),
                d.getCinemaRoom() != null ? d.getCinemaRoom().getCinemaRoomName() : "N/A",
                String.format("%,d", d.getRoomRate()),
                d.getActiveDate() != null ? d.getActiveDate().toString() : "N/A",
                d.getRoomDescription() != null ? d.getRoomDescription() : ""
            ));
        }
        TableRenderer.printTable(title, headers, rows);
    }

    private static boolean printRoomsSummary() {
        List<CinemaRoom> list = roomDao.getAllRooms();
        if (list.isEmpty()) {
            System.out.println("⚠️ [Notice] No cinema rooms found.");
            return false;
        }
        displayRooms("All Cinema Rooms", list);
        return true;
    }

    private static boolean printSeatsSummary() {
        List<Seat> list = seatDao.getAllSeats();
        if (list.isEmpty()) {
            System.out.println("⚠️ [Notice] No seats found.");
            return false;
        }
        displaySeats("All Seats", list);
        return true;
    }

    private static boolean printDetailsSummary() {
        List<CinemaRoomDetail> list = detailDao.getAllRoomDetails();
        if (list.isEmpty()) {
            System.out.println("⚠️ [Notice] No room details found.");
            return false;
        }
        displayDetails("All Room Details", list);
        return true;
    }
}
