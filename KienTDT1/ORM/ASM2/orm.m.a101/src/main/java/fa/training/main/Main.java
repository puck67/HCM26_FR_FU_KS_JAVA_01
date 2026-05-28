package fa.training.main;

import fa.training.dao.CinemaRoomDAO;
import fa.training.dao.CinemaRoomDetailDAO;
import fa.training.dao.SeatDAO;
import fa.training.dao.impl.CinemaRoomDAOImpl;
import fa.training.dao.impl.CinemaRoomDetailDAOImpl;
import fa.training.dao.impl.SeatDAOImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static CinemaRoomDAO roomDAO =
            new CinemaRoomDAOImpl();

    static CinemaRoomDetailDAO detailDAO =
            new CinemaRoomDetailDAOImpl();

    static SeatDAO seatDAO =
            new SeatDAOImpl();

    public static void main(String[] args) {

        int choice;

        do {

            System.out.println("\n===== MOVIE THEATER MANAGEMENT =====");

            System.out.println("1. Insert Cinema Room");
            System.out.println("2. View All Cinema Rooms");
            System.out.println("3. Find Room By ID");
            System.out.println("4. Update Room");
            System.out.println("5. Delete Room");
            System.out.println("6. Insert Seat");
            System.out.println("7. View All Seats");
            System.out.println("8. Exit");

            System.out.print("Choose: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    insertRoom();
                    break;

                case 2:
                    viewAllRooms();
                    break;

                case 3:
                    findRoomById();
                    break;

                case 4:
                    updateRoom();
                    break;

                case 5:
                    deleteRoom();
                    break;

                case 6:
                    insertSeat();
                    break;

                case 7:
                    viewAllSeats();
                    break;

                case 8:
                    System.out.println("EXIT...");
                    break;

                default:
                    System.out.println("Invalid choice");
            }

        } while (choice != 8);
    }

    // ================= ROOM =================

    public static void insertRoom() {

        System.out.print("Room name: ");
        String name = sc.nextLine();

        System.out.print("Seat quantity: ");
        int quantity = Integer.parseInt(sc.nextLine());

        CinemaRoom room =
                new CinemaRoom(name, quantity);

        // detail
        System.out.print("Room rate: ");
        int rate = Integer.parseInt(sc.nextLine());

        System.out.print("Description: ");
        String description = sc.nextLine();

        CinemaRoomDetail detail =
                new CinemaRoomDetail(
                        rate,
                        LocalDate.now(),
                        description
                );

        // relationship
        detail.setCinemaRoom(room);

        room.setCinemaRoomDetail(detail);

        roomDAO.insert(room);

        System.out.println("INSERT ROOM SUCCESS");
    }

    public static void viewAllRooms() {

        List<CinemaRoom> list =
                roomDAO.getAll();

        for (CinemaRoom room : list) {

            System.out.println("-------------------");

            System.out.println("ID: "
                    + room.getCinemaRoomId());

            System.out.println("Name: "
                    + room.getCinemaRoomName());

            System.out.println("Seat Quantity: "
                    + room.getSeatQuantity());
        }
    }

    public static void findRoomById() {

        System.out.print("Enter ID: ");
        int id = Integer.parseInt(sc.nextLine());

        CinemaRoom room =
                roomDAO.getById(id);

        if (room == null) {

            System.out.println("Room not found");
            return;
        }

        System.out.println("ID: "
                + room.getCinemaRoomId());

        System.out.println("Name: "
                + room.getCinemaRoomName());

        System.out.println("Seat Quantity: "
                + room.getSeatQuantity());
    }

    public static void updateRoom() {

        System.out.print("Enter room ID: ");
        int id = Integer.parseInt(sc.nextLine());

        CinemaRoom room =
                roomDAO.getById(id);

        if (room == null) {

            System.out.println("Room not found");
            return;
        }

        System.out.print("New room name: ");
        String name = sc.nextLine();

        System.out.print("New seat quantity: ");
        int quantity =
                Integer.parseInt(sc.nextLine());

        room.setCinemaRoomName(name);
        room.setSeatQuantity(quantity);

        roomDAO.update(room);

        System.out.println("UPDATE SUCCESS");
    }

    public static void deleteRoom() {

        System.out.print("Enter room ID: ");
        int id = Integer.parseInt(sc.nextLine());

        roomDAO.delete(id);

        System.out.println("DELETE SUCCESS");
    }

    // ================= SEAT =================

    public static void insertSeat() {

        System.out.print("Room ID: ");
        int roomId =
                Integer.parseInt(sc.nextLine());

        CinemaRoom room =
                roomDAO.getById(roomId);

        if (room == null) {

            System.out.println("Room not found");
            return;
        }

        System.out.print("Seat Column: ");
        String column = sc.nextLine();

        System.out.print("Seat Row: ");
        int row =
                Integer.parseInt(sc.nextLine());

        System.out.print("Seat Status: ");
        String status = sc.nextLine();

        System.out.print("Seat Type: ");
        String type = sc.nextLine();

        Seat seat =
                new Seat(column,
                        row,
                        status,
                        type);

        // relationship
        seat.setCinemaRoom(room);

        seatDAO.insert(seat);

        System.out.println("INSERT SEAT SUCCESS");
    }

    public static void viewAllSeats() {

        List<Seat> list =
                seatDAO.getAll();

        for (Seat seat : list) {

            System.out.println("----------------");

            System.out.println("Seat ID: "
                    + seat.getSeatId());

            System.out.println("Column: "
                    + seat.getSeatColumn());

            System.out.println("Row: "
                    + seat.getSeatRow());

            System.out.println("Status: "
                    + seat.getSeatStatus());

            System.out.println("Type: "
                    + seat.getSeatType());

            System.out.println("Room: "
                    + seat.getCinemaRoom()
                    .getCinemaRoomName());
        }
    }
}