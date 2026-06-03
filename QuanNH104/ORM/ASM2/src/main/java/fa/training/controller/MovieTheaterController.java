package fa.training.controller;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MovieTheaterController {

    private final RoomDao roomDao = new RoomDao();
    private final RoomDetailDao roomDetailDao = new RoomDetailDao();
    private final SeatDao seatDao = new SeatDao();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void addCinemaRoom(Scanner scanner) {
        try {
            System.out.print("Enter cinema room name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter seat quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            CinemaRoom room = new CinemaRoom(name, quantity);
            roomDao.insertCinemaRoom(room);
            System.out.println("Cinema room added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listCinemaRooms() {
        List<CinemaRoom> rooms = roomDao.getAllCinemaRooms();
        if (rooms.isEmpty()) {
            System.out.println("No cinema rooms found.");
            return;
        }
        System.out.println("+------+-------------------------------------+---------------+");
        System.out.printf("| %-4s | %-35s | %-13s |%n", "ID", "Room Name", "Seat Quantity");
        System.out.println("+------+-------------------------------------+---------------+");
        rooms.forEach(room -> System.out.printf("| %-4d | %-35s | %-13d |%n", 
                room.getCinemaRoomId(), room.getCinemaRoomName(), room.getSeatQuantity()));
        System.out.println("+------+-------------------------------------+---------------+");
    }

    public void updateCinemaRoom(Scanner scanner) {
        try {
            System.out.print("Enter cinema room ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            CinemaRoom room = roomDao.getCinemaRoomById(id);
            if (room == null) {
                System.out.println("Cinema room not found!");
                return;
            }
            System.out.print("Enter new cinema room name (current: " + room.getCinemaRoomName() + "): ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter new seat quantity (current: " + room.getSeatQuantity() + "): ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            room.setCinemaRoomName(name);
            room.setSeatQuantity(quantity);
            roomDao.updateCinemaRoom(room);
            System.out.println("Cinema room updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteCinemaRoom(Scanner scanner) {
        try {
            System.out.print("Enter cinema room ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            roomDao.deleteCinemaRoomById(id);
            System.out.println("Cinema room deleted successfully (if existed)!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchCinemaRoom(Scanner scanner) {
        try {
            System.out.print("Enter cinema room ID to search: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            CinemaRoom room = roomDao.getCinemaRoomById(id);
            if (room != null) {
                System.out.println("\n=== CINEMA ROOM INFO ===");
                System.out.println("ID: " + room.getCinemaRoomId());
                System.out.println("Name: " + room.getCinemaRoomName());
                System.out.println("Seat Quantity: " + room.getSeatQuantity());
                
                if (room.getCinemaRoomDetail() != null) {
                    System.out.println("\n=== DETAIL ===");
                    System.out.println("  Rate: " + room.getCinemaRoomDetail().getRoomRate());
                    System.out.println("  Active Date: " + room.getCinemaRoomDetail().getActiveDate());
                    System.out.println("  Description: " + room.getCinemaRoomDetail().getRoomDescription());
                }
                
                if (room.getSeats() != null && !room.getSeats().isEmpty()) {
                    System.out.println("\n=== SEATS ===");
                    System.out.println("+------+--------+--------+---------------+---------------+");
                    System.out.printf("| %-4s | %-6s | %-6s | %-13s | %-13s |%n", "ID", "Row", "Column", "Status", "Type");
                    System.out.println("+------+--------+--------+---------------+---------------+");
                    room.getSeats().forEach(seat -> System.out.printf("| %-4d | %-6d | %-6s | %-13s | %-13s |%n", 
                        seat.getSeatId(), seat.getSeatRow(), seat.getSeatColumn(), seat.getSeatStatus(), seat.getSeatType()));
                    System.out.println("+------+--------+--------+---------------+---------------+");
                }
            } else {
                System.out.println("Cinema room not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addCinemaRoomDetail(Scanner scanner) {
        try {
            System.out.print("Enter cinema room ID to attach detail: ");
            int roomId = Integer.parseInt(scanner.nextLine().trim());
            CinemaRoom room = roomDao.getCinemaRoomById(roomId);
            if (room == null) {
                System.out.println("Cinema room not found!");
                return;
            }
            System.out.print("Enter room rate: ");
            int rate = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter active date (yyyy-MM-dd): ");
            LocalDate activeDate = LocalDate.parse(scanner.nextLine().trim(), formatter);
            System.out.print("Enter room description: ");
            String desc = scanner.nextLine().trim();

            CinemaRoomDetail detail = new CinemaRoomDetail(room, rate, activeDate, desc);
            roomDetailDao.insertCinemaRoomDetail(detail);
            System.out.println("Cinema room detail added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listCinemaRoomDetails() {
        List<CinemaRoomDetail> details = roomDetailDao.getAllCinemaRoomDetails();
        if (details.isEmpty()) {
            System.out.println("No cinema room details found.");
            return;
        }
        System.out.println("+------+------------+------------+--------------------------------------------------");
        System.out.printf("| %-4s | %-10s | %-10s | %-48s |%n", "ID", "Room Name", "Rate", "Description");
        System.out.println("+------+------------+------------+--------------------------------------------------");
        details.forEach(detail -> {
            String roomName = detail.getCinemaRoom() != null ? detail.getCinemaRoom().getCinemaRoomName() : "N/A";
            System.out.printf("| %-4d | %-10s | %-10d | %-48s |%n", 
                detail.getCinemaRoomDetailId(), roomName, detail.getRoomRate(), detail.getRoomDescription());
        });
        System.out.println("+------+------------+------------+--------------------------------------------------");
    }

    public void updateCinemaRoomDetail(Scanner scanner) {
        try {
            System.out.print("Enter cinema room detail ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            CinemaRoomDetail detail = roomDetailDao.getCinemaRoomDetailById(id);
            if (detail == null) {
                System.out.println("Cinema room detail not found!");
                return;
            }
            System.out.print("Enter new room rate (current: " + detail.getRoomRate() + "): ");
            int rate = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter new active date (yyyy-MM-dd) (current: " + detail.getActiveDate() + "): ");
            LocalDate activeDate = LocalDate.parse(scanner.nextLine().trim(), formatter);
            System.out.print("Enter new room description (current: " + detail.getRoomDescription() + "): ");
            String desc = scanner.nextLine().trim();

            detail.setRoomRate(rate);
            detail.setActiveDate(activeDate);
            detail.setRoomDescription(desc);
            roomDetailDao.updateCinemaRoomDetail(detail);
            System.out.println("Cinema room detail updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteCinemaRoomDetail(Scanner scanner) {
        try {
            System.out.print("Enter cinema room detail ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            roomDetailDao.deleteCinemaRoomDetailById(id);
            System.out.println("Cinema room detail deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchCinemaRoomDetail(Scanner scanner) {
        try {
            System.out.print("Enter cinema room detail ID to search: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            CinemaRoomDetail detail = roomDetailDao.getCinemaRoomDetailById(id);
            if (detail != null) {
                System.out.println("\n=== CINEMA ROOM DETAIL INFO ===");
                System.out.println("ID: " + detail.getCinemaRoomDetailId());
                System.out.println("Rate: " + detail.getRoomRate());
                System.out.println("Active Date: " + detail.getActiveDate());
                System.out.println("Description: " + detail.getRoomDescription());
                if (detail.getCinemaRoom() != null) {
                    System.out.println("Associated Room ID: " + detail.getCinemaRoom().getCinemaRoomId());
                    System.out.println("Associated Room Name: " + detail.getCinemaRoom().getCinemaRoomName());
                }
            } else {
                System.out.println("Cinema room detail not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addSeat(Scanner scanner) {
        try {
            System.out.print("Enter cinema room ID: ");
            int roomId = Integer.parseInt(scanner.nextLine().trim());
            CinemaRoom room = roomDao.getCinemaRoomById(roomId);
            if (room == null) {
                System.out.println("Cinema room not found!");
                return;
            }
            System.out.print("Enter seat column (e.g. A, B, C): ");
            String column = scanner.nextLine().trim();
            System.out.print("Enter seat row (number): ");
            int row = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter seat status (Available/Not Available/Booked): ");
            String status = scanner.nextLine().trim();
            System.out.print("Enter seat type (VIP/Normal): ");
            String type = scanner.nextLine().trim();

            Seat seat = new Seat(room, column, row, status, type);
            seatDao.insertSeat(seat);
            System.out.println("Seat added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listSeats() {
        List<Seat> seats = seatDao.getAllSeats();
        if (seats.isEmpty()) {
            System.out.println("No seats found.");
            return;
        }
        System.out.println("+------+------------+--------+--------+---------------+---------------+");
        System.out.printf("| %-4s | %-10s | %-6s | %-6s | %-13s | %-13s |%n", "ID", "Room Name", "Row", "Column", "Status", "Type");
        System.out.println("+------+------------+--------+--------+---------------+---------------+");
        seats.forEach(seat -> {
            String roomName = seat.getCinemaRoom() != null ? seat.getCinemaRoom().getCinemaRoomName() : "N/A";
            System.out.printf("| %-4d | %-10s | %-6d | %-6s | %-13s | %-13s |%n", 
                seat.getSeatId(), roomName, seat.getSeatRow(), seat.getSeatColumn(), seat.getSeatStatus(), seat.getSeatType());
        });
        System.out.println("+------+------------+--------+--------+---------------+---------------+");
    }

    public void updateSeat(Scanner scanner) {
        try {
            System.out.print("Enter seat ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Seat seat = seatDao.getSeatById(id);
            if (seat == null) {
                System.out.println("Seat not found!");
                return;
            }
            System.out.print("Enter new seat column (current: " + seat.getSeatColumn() + "): ");
            String column = scanner.nextLine().trim();
            System.out.print("Enter new seat row (current: " + seat.getSeatRow() + "): ");
            int row = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter new seat status (Available/Not Available/Booked) (current: " + seat.getSeatStatus() + "): ");
            String status = scanner.nextLine().trim();
            System.out.print("Enter new seat type (VIP/Normal) (current: " + seat.getSeatType() + "): ");
            String type = scanner.nextLine().trim();

            seat.setSeatColumn(column);
            seat.setSeatRow(row);
            seat.setSeatStatus(status);
            seat.setSeatType(type);
            seatDao.updateSeat(seat);
            System.out.println("Seat updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteSeat(Scanner scanner) {
        try {
            System.out.print("Enter seat ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            seatDao.deleteSeatById(id);
            System.out.println("Seat deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchSeat(Scanner scanner) {
        try {
            System.out.print("Enter seat ID to search: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Seat seat = seatDao.getSeatById(id);
            if (seat != null) {
                System.out.println("\n=== SEAT INFO ===");
                System.out.println("ID: " + seat.getSeatId());
                System.out.println("Row: " + seat.getSeatRow());
                System.out.println("Column: " + seat.getSeatColumn());
                System.out.println("Status: " + seat.getSeatStatus());
                System.out.println("Type: " + seat.getSeatType());
                if (seat.getCinemaRoom() != null) {
                    System.out.println("In Room ID: " + seat.getCinemaRoom().getCinemaRoomId());
                    System.out.println("In Room Name: " + seat.getCinemaRoom().getCinemaRoomName());
                }
            } else {
                System.out.println("Seat not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteRoomByNameIfExists(String name) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<CinemaRoom> rooms = session.createQuery("from CinemaRoom where cinemaRoomName = :name", CinemaRoom.class)
                    .setParameter("name", name)
                    .list();
            for (CinemaRoom room : rooms) {
                session.remove(room);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void runScenario() {
        try {
            deleteRoomByNameIfExists("Room Sim 01");
            deleteRoomByNameIfExists("Room Sim 02");
            deleteRoomByNameIfExists("Room Sim 01 - Updated");

            System.out.println("\n--- RUNNING SCENARIO SIMULATION ---");

            System.out.println("Step 1: Inserting 2 Cinema Rooms...");
            CinemaRoom room1 = new CinemaRoom("Room Sim 01", 10);
            CinemaRoom room2 = new CinemaRoom("Room Sim 02", 20);
            roomDao.insertCinemaRoom(room1);
            roomDao.insertCinemaRoom(room2);

            System.out.println("Step 2: Inserting Cinema Room Details...");
            CinemaRoomDetail detail1 = new CinemaRoomDetail(room1, 80000, LocalDate.now(), "Standard room with Dolby Sound");
            CinemaRoomDetail detail2 = new CinemaRoomDetail(room2, 120000, LocalDate.now().plusDays(1), "VIP room with IMAX screen");
            roomDetailDao.insertCinemaRoomDetail(detail1);
            roomDetailDao.insertCinemaRoomDetail(detail2);

            System.out.println("Step 3: Inserting Seats for Room 01...");
            Seat seat1 = new Seat(room1, "A", 1, "Available", "Normal");
            Seat seat2 = new Seat(room1, "A", 2, "Booked", "VIP");
            Seat seat3 = new Seat(room1, "B", 1, "Not Available", "Normal");
            seatDao.insertSeat(seat1);
            seatDao.insertSeat(seat2);
            seatDao.insertSeat(seat3);

            System.out.println("Step 4: Listing all database records...");
            System.out.println("Rooms:");
            listCinemaRooms();
            System.out.println("Room Details:");
            listCinemaRoomDetails();
            System.out.println("Seats:");
            listSeats();

            System.out.println("Step 5: Updating Room 01 Name...");
            room1.setCinemaRoomName("Room Sim 01 - Updated");
            roomDao.updateCinemaRoom(room1);
            System.out.println("  Updated Room:");
            searchCinemaRoom(new Scanner(String.valueOf(room1.getCinemaRoomId())));

            System.out.println("Step 6: Updating Seat 2 Status...");
            seat2.setSeatStatus("Available");
            seatDao.updateSeat(seat2);
            System.out.println("  Updated Seat:");
            searchSeat(new Scanner(String.valueOf(seat2.getSeatId())));

            System.out.println("Step 7: Clean up simulation data...");
            roomDao.deleteCinemaRoomById(room1.getCinemaRoomId());
            roomDao.deleteCinemaRoomById(room2.getCinemaRoomId());

            System.out.println("--- SCENARIO SIMULATION FINISHED SUCCESSFULLY ---");
        } catch (Exception e) {
            System.out.println("Scenario failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
