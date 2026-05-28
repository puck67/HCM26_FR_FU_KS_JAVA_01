package fa.training;

import fa.training.dao.RoomDAO;
import fa.training.dao.RoomDetailDAO;
import fa.training.dao.SeatDAO;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtil;
import fa.training.utils.InputValidator;

import java.time.LocalDate;
import java.util.List;

public class App {

    private static final RoomDAO roomDAO = new RoomDAO();
    private static final RoomDetailDAO roomDetailDAO = new RoomDetailDAO();
    private static final SeatDAO seatDAO = new SeatDAO();

    public static void main(String[] args) {
        boolean running = true;
        
        while (running) {
            System.out.println("\n================================================");
            System.out.println("  Movie Theater Management System");
            System.out.println("================================================");
            System.out.println("1. Add New Room (Create)");
            System.out.println("2. Update Room (Update)");
            System.out.println("3. View All Rooms (Read - Java 8 Streams)");
            System.out.println("4. Find Room by ID");
            System.out.println("5. Run Default Simulation Scenario");
            System.out.println("0. Exit");
            System.out.println("================================================");
            
            int choice = InputValidator.getInt("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    createRoom();
                    break;
                case 2:
                    updateRoom();
                    break;
                case 3:
                    viewAllRooms();
                    break;
                case 4:
                    findRoomById();
                    break;
                case 5:
                    runSimulation();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        HibernateUtil.shutdown();
    }

    private static void createRoom() {
        System.out.println("\n--- ADD NEW ROOM ---");
        
        String name;
        while (true) {
            name = InputValidator.getString("Enter Room Name: ");
            if (roomDAO.isRoomNameExists(name, null)) {
                System.out.println("Error: Room name '" + name + "' already exists in Database! Please choose another name.");
            } else {
                break; // Name is valid and unique
            }
        }
        
        int seatQuantity = InputValidator.getInt("Enter Seat Quantity: ");
        int rate = InputValidator.getInt("Enter Room Rate (e.g. 150000): ");
        LocalDate activeDate = InputValidator.getLocalDate("Enter Active Date (YYYY-MM-DD): ");
        String description = InputValidator.getString("Enter Description: ");
        
        CinemaRoom room = new CinemaRoom(name, seatQuantity);
        CinemaRoomDetail detail = new CinemaRoomDetail(rate, activeDate, description);
        room.setDetail(detail);
        
        // Add one default seat just as an example
        Seat seat1 = new Seat("A", 1, "Available", "Normal");
        room.addSeat(seat1);
        
        try {
            roomDAO.insertRoom(room);
            System.out.println("Room '" + name + "' created successfully!");
        } catch (Exception e) {
            System.out.println("Error saving room: " + e.getMessage());
        }
    }

    private static void updateRoom() {
        System.out.println("\n--- UPDATE ROOM ---");
        int id = InputValidator.getInt("Enter Room ID to update: ");
        CinemaRoom room = roomDAO.getRoomByID(id);
        
        if (room == null) {
            System.out.println("Room ID " + id + " not found!");
            return;
        }
        
        System.out.println("Updating Room: " + room.getCinemaRoomName());
        
        String newName;
        while (true) {
            newName = InputValidator.getString("Enter New Room Name: ");
            if (roomDAO.isRoomNameExists(newName, id)) {
                System.out.println("Error: Room name '" + newName + "' already exists in Database! Please choose another name.");
            } else {
                break;
            }
        }
        
        int seatQuantity = InputValidator.getInt("Enter New Seat Quantity: ");
        
        try {
            roomDAO.updateRoomByID(id, newName, seatQuantity);
            
            // If we also want to update detail:
            if (room.getCinemaRoomDetail() != null) {
                int rate = InputValidator.getInt("Enter New Room Rate: ");
                LocalDate activeDate = InputValidator.getLocalDate("Enter New Active Date (YYYY-MM-DD): ");
                String description = InputValidator.getString("Enter New Description: ");
                roomDetailDAO.updateRoomDetailByID(room.getCinemaRoomDetail().getCinemaRoomDetailId(), rate, activeDate, description);
            }
            
            System.out.println("Room ID " + id + " updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating room: " + e.getMessage());
        }
    }

    private static void viewAllRooms() {
        System.out.println("\n--- ALL ROOMS ---");
        List<CinemaRoom> rooms = roomDAO.getAllRooms();
        
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("No rooms found in database.");
            return;
        }
        
        // Utilizing Java 8 Stream API and Lambda Expressions as required
        rooms.stream()
             .sorted((r1, r2) -> r1.getCinemaRoomName().compareToIgnoreCase(r2.getCinemaRoomName()))
             .forEach(r -> {
                 System.out.println(r);
                 if (r.getCinemaRoomDetail() != null) {
                     System.out.println("   Detail: " + r.getCinemaRoomDetail());
                 }
             });
    }

    private static void findRoomById() {
        System.out.println("\n--- FIND ROOM ---");
        int id = InputValidator.getInt("Enter Room ID to search: ");
        
        CinemaRoom room = roomDAO.getRoomByID(id);
        if (room != null) {
            System.out.println("Found: " + room);
            if (room.getCinemaRoomDetail() != null) {
                System.out.println("Detail: " + room.getCinemaRoomDetail());
            }
            List<Seat> seats = seatDAO.getSeatsByRoomID(id);
            if (seats != null && !seats.isEmpty()) {
                System.out.println("Seats in this room:");
                seats.forEach(s -> System.out.println("  - " + s));
            } else {
                System.out.println("No seats mapped to this room.");
            }
        } else {
            System.out.println("Room ID " + id + " not found!");
        }
    }

    private static void runSimulation() {
        System.out.println("\n--- RUNNING DEFAULT SIMULATION ---");
        try {
            CinemaRoom room = new CinemaRoom("Hall Premium " + System.currentTimeMillis(), 4);
            
            Seat seat1 = new Seat("A", 1, "Available", "Normal");
            Seat seat2 = new Seat("A", 2, "Available", "Normal");
            Seat seat3 = new Seat("B", 1, "Available", "VIP");
            Seat seat4 = new Seat("B", 2, "Available", "VIP");
            
            room.addSeat(seat1);
            room.addSeat(seat2);
            room.addSeat(seat3);
            room.addSeat(seat4);
            
            CinemaRoomDetail detail = new CinemaRoomDetail(150_000, LocalDate.now(), "Main hall with 4K projector");
            room.setDetail(detail);
            
            roomDAO.insertRoom(room);
            System.out.println("Simulation data created successfully!");
            
            seatDAO.updateSeatByID(seat1.getSeatId(), "Booked", "Normal");
            System.out.println("Booked seat ID: " + seat1.getSeatId());
            
        } catch (Exception e) {
            System.out.println("Error during simulation: " + e.getMessage());
        }
    }
}
