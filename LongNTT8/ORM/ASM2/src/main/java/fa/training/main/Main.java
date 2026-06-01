package fa.training.main;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import fa.training.util.InputUtil;

import java.time.LocalDate;

public class Main {
    private static final RoomDao roomDao = new RoomDao();
    private static final RoomDetailDao detailDao = new RoomDetailDao();
    private static final SeatDao seatDao = new SeatDao();

    public static void main(String[] args) {
        // Initialize Hibernate session factory
        System.out.println("Initializing Hibernate...");
        HibernateUtil.getSessionFactory();

        Menu mainMenu = new Menu("Cinema Management System");

        mainMenu.addItem("Manage Cinema Rooms", () -> getRoomMenu().display());
        mainMenu.addItem("Manage Cinema Room Details", () -> getRoomDetailMenu().display());
        mainMenu.addItem("Manage Seats", () -> getSeatMenu().display());

        mainMenu.display();

        // Shutdown Hibernate
        HibernateUtil.shutdown();
    }

    private static Menu getRoomMenu() {
        Menu menu = new Menu("Manage Cinema Rooms");

        menu.addItem("Add Cinema Room", () -> {
            String name = InputUtil.getString("Enter Room Name: ");
            int quantity = InputUtil.getInt("Enter Seat Quantity: ");
            CinemaRoom room = new CinemaRoom(name, quantity);
            if (roomDao.insert(room)) {
                System.out.println("Room added successfully.");
            } else {
                System.out.println("Failed to add room.");
            }
        });

        menu.addItem("View All Cinema Rooms", () -> {
            System.out.println("--- Cinema Rooms ---");
            roomDao.getAll().forEach(System.out::println);
        });

        menu.addItem("Find Cinema Room by ID", () -> {
            int id = InputUtil.getInt("Enter Room ID: ");
            CinemaRoom room = roomDao.getById(id);
            if (room != null) {
                System.out.println("Found: " + room);
            } else {
                System.out.println("Room not found.");
            }
        });

        menu.addItem("Update Cinema Room by ID", () -> {
            int id = InputUtil.getInt("Enter Room ID to update: ");
            CinemaRoom room = roomDao.getById(id);
            if (room != null) {
                String newName = InputUtil.getString("Enter new Room Name: ");
                int newQuantity = InputUtil.getInt("Enter new Seat Quantity: ");
                room.setCinemaRoomName(newName);
                room.setSeatQuantity(newQuantity);
                if (roomDao.updateById(room)) {
                    System.out.println("Room updated successfully.");
                } else {
                    System.out.println("Failed to update room.");
                }
            } else {
                System.out.println("Room not found.");
            }
        });

        menu.addItem("Delete Cinema Room by ID", () -> {
            int id = InputUtil.getInt("Enter Room ID to delete: ");
            if (roomDao.deleteById(id)) {
                System.out.println("Room deleted successfully.");
            } else {
                System.out.println("Failed to delete room (or room not found).");
            }
        });

        return menu;
    }

    private static Menu getRoomDetailMenu() {
        Menu menu = new Menu("Manage Cinema Room Details");

        menu.addItem("Add Room Detail", () -> {
            int roomId = InputUtil.getInt("Enter existing Cinema Room ID: ");
            CinemaRoom room = roomDao.getById(roomId);
            if (room == null) {
                System.out.println("Cinema Room not found.");
                return;
            }
            int rate = InputUtil.getInt("Enter Room Rate: ");
            LocalDate date = InputUtil.getLocalDate("Enter Active Date");
            String description = InputUtil.getString("Enter Room Description: ");
            CinemaRoomDetail detail = new CinemaRoomDetail(room, rate, date, description);
            if (detailDao.insert(detail)) {
                System.out.println("Detail added successfully.");
            } else {
                System.out.println("Failed to add detail.");
            }
        });

        menu.addItem("View All Room Details", () -> {
            System.out.println("--- Room Details ---");
            detailDao.getAll().forEach(System.out::println);
        });

        menu.addItem("Update Room Detail by ID", () -> {
            int id = InputUtil.getInt("Enter Room Detail ID to update: ");
            CinemaRoomDetail detail = detailDao.getById(id);
            if (detail != null) {
                int rate = InputUtil.getInt("Enter new Room Rate: ");
                LocalDate date = InputUtil.getLocalDate("Enter new Active Date");
                String description = InputUtil.getString("Enter new Room Description: ");
                detail.setRoomRate(rate);
                detail.setActiveDate(date);
                detail.setRoomDescription(description);
                if (detailDao.updateById(detail)) {
                    System.out.println("Detail updated successfully.");
                } else {
                    System.out.println("Failed to update detail.");
                }
            } else {
                System.out.println("Detail not found.");
            }
        });

        menu.addItem("Delete Room Detail by ID", () -> {
            int id = InputUtil.getInt("Enter Room Detail ID to delete: ");
            if (detailDao.deleteById(id)) {
                System.out.println("Detail deleted successfully.");
            } else {
                System.out.println("Failed to delete detail (or not found).");
            }
        });

        return menu;
    }

    private static Menu getSeatMenu() {
        Menu menu = new Menu("Manage Seats");

        menu.addItem("Add Seat", () -> {
            int roomId = InputUtil.getInt("Enter existing Cinema Room ID: ");
            CinemaRoom room = roomDao.getById(roomId);
            if (room == null) {
                System.out.println("Cinema Room not found.");
                return;
            }
            String col = InputUtil.getString("Enter Seat Column: ");
            int row = InputUtil.getInt("Enter Seat Row: ");
            String status = InputUtil.getSeatStatus("Enter Seat Status");
            String type = InputUtil.getSeatType("Enter Seat Type");
            Seat seat = new Seat(room, col, row, status, type);
            if (seatDao.insert(seat)) {
                System.out.println("Seat added successfully.");
            } else {
                System.out.println("Failed to add seat.");
            }
        });

        menu.addItem("View All Seats", () -> {
            System.out.println("--- Seats ---");
            seatDao.getAll().forEach(System.out::println);
        });

        menu.addItem("Update Seat by ID", () -> {
            int id = InputUtil.getInt("Enter Seat ID to update: ");
            Seat seat = seatDao.getById(id);
            if (seat != null) {
                String status = InputUtil.getSeatStatus("Enter new Seat Status");
                String type = InputUtil.getSeatType("Enter new Seat Type");
                seat.setSeatStatus(status);
                seat.setSeatType(type);
                if (seatDao.updateById(seat)) {
                    System.out.println("Seat updated successfully.");
                } else {
                    System.out.println("Failed to update seat.");
                }
            } else {
                System.out.println("Seat not found.");
            }
        });

        menu.addItem("Delete Seat by ID", () -> {
            int id = InputUtil.getInt("Enter Seat ID to delete: ");
            if (seatDao.deleteById(id)) {
                System.out.println("Seat deleted successfully.");
            } else {
                System.out.println("Failed to delete seat (or not found).");
            }
        });

        return menu;
    }
}
