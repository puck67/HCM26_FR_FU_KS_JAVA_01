package fa.training.ui;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.service.RoomDetailService;
import fa.training.service.RoomService;
import fa.training.service.SeatService;
import fa.training.util.Validator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuHandler {

    private final Scanner scanner;
    private final RoomService roomService = new RoomService();
    private final RoomDetailService roomDetailService = new RoomDetailService();
    private final SeatService seatService = new SeatService();

    public MenuHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        boolean running = true;
        ConsoleUI.printMainBanner();
        while (running) {
            ConsoleUI.printPrompt("Select option");
            String choice = scanner.nextLine().trim();
            boolean valid = true;
            switch (choice) {
                case "1" -> handleRoomMenu();
                case "2" -> handleDetailMenu();
                case "3" -> handleSeatMenu();
                case "0" -> {
                    ConsoleUI.printInfo("Goodbye!");
                    running = false;
                }
                default -> {
                    ConsoleUI.printError("Invalid option. Please enter 0-3.");
                    valid = false;
                }
            }
            if (valid && running)
                ConsoleUI.printMainBanner();
        }
    }

    private void handleRoomMenu() {
        boolean back = false;
        ConsoleUI.printRoomMenu();
        while (!back) {
            ConsoleUI.printPrompt("Select option");
            String choice = scanner.nextLine().trim();
            boolean valid = true;
            switch (choice) {
                case "1" -> listAllRooms();
                case "2" -> findRoomById();
                case "3" -> addRoom();
                case "4" -> updateRoom();
                case "5" -> deleteRoom();
                case "0" -> back = true;
                default -> {
                    ConsoleUI.printError("Invalid option. Please enter 0-5.");
                    valid = false;
                }
            }
            if (valid && !back)
                ConsoleUI.printRoomMenu();
        }
    }

    private void handleDetailMenu() {
        boolean back = false;
        ConsoleUI.printDetailMenu();
        while (!back) {
            ConsoleUI.printPrompt("Select option");
            String choice = scanner.nextLine().trim();
            boolean valid = true;
            switch (choice) {
                case "1" -> listAllDetails();
                case "2" -> findDetailById();
                case "3" -> findDetailByRoomId();
                case "4" -> addDetail();
                case "5" -> updateDetail();
                case "6" -> deleteDetail();
                case "0" -> back = true;
                default -> {
                    ConsoleUI.printError("Invalid option. Please enter 0-6.");
                    valid = false;
                }
            }
            if (valid && !back)
                ConsoleUI.printDetailMenu();
        }
    }

    private void handleSeatMenu() {
        boolean back = false;
        ConsoleUI.printSeatMenu();
        while (!back) {
            ConsoleUI.printPrompt("Select option");
            String choice = scanner.nextLine().trim();
            boolean valid = true;
            switch (choice) {
                case "1" -> listAllSeats();
                case "2" -> findSeatById();
                case "3" -> findSeatsByRoomId();
                case "4" -> findSeatsByStatus();
                case "5" -> findSeatsByType();
                case "6" -> addSeat();
                case "7" -> updateSeat();
                case "8" -> deleteSeat();
                case "0" -> back = true;
                default -> {
                    ConsoleUI.printError("Invalid option. Please enter 0-8.");
                    valid = false;
                }
            }
            if (valid && !back)
                ConsoleUI.printSeatMenu();
        }
    }

    private int readInt(String prompt) {
        while (true) {
            ConsoleUI.printPrompt(prompt);
            Optional<Integer> val = Validator.parseIntOptional(scanner.nextLine());
            if (val.isPresent())
                return val.get();
            ConsoleUI.printError("Must be a valid integer. Try again.");
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int val = readInt(prompt);
            if (val > 0)
                return val;
            ConsoleUI.printError("Must be greater than 0. Try again.");
        }
    }

    private String readNonBlank(String prompt) {
        while (true) {
            ConsoleUI.printPrompt(prompt);
            String val = scanner.nextLine().trim();
            if (!val.isEmpty())
                return val;
            ConsoleUI.printError("Cannot be blank. Try again.");
        }
    }

    private String readSeatStatus() {
        while (true) {
            ConsoleUI.printInfo("Options: Available | Not Available | Booked");
            ConsoleUI.printPrompt("Seat Status");
            String val = scanner.nextLine().trim();
            try {
                return Validator.requireSeatStatus(val);
            } catch (IllegalArgumentException ex) {
                ConsoleUI.printError(ex.getMessage() + " Try again.");
            }
        }
    }

    private String readSeatType() {
        while (true) {
            ConsoleUI.printInfo("Options: VIP | Normal");
            ConsoleUI.printPrompt("Seat Type");
            String val = scanner.nextLine().trim();
            try {
                return Validator.requireSeatType(val);
            } catch (IllegalArgumentException ex) {
                ConsoleUI.printError(ex.getMessage() + " Try again.");
            }
        }
    }

    private String readDate(String prompt) {
        while (true) {
            ConsoleUI.printPrompt(prompt + " (yyyy-MM-dd)");
            String val = scanner.nextLine().trim();
            try {
                Validator.parseDate(val);
                return val;
            } catch (IllegalArgumentException ex) {
                ConsoleUI.printError(ex.getMessage() + " Try again.");
            }
        }
    }

    private void listAllRooms() {
        List<CinemaRoom> rooms = roomService.getAllRooms();
        ConsoleUI.printRoomTable(rooms);
        pause();
    }

    private void findRoomById() {
        int id = readPositiveInt("Room ID");
        roomService.getRoomById(id).ifPresentOrElse(
                r -> {
                    ConsoleUI.printRoomDetail(r);
                    pause();
                },
                () -> ConsoleUI.printError("Room with id=" + id + " not found."));
    }

    private void addRoom() {
        ConsoleUI.printSectionHeader("ADD NEW CINEMA ROOM");
        String name = readNonBlank("Room Name");
        int qty = readPositiveInt("Seat Quantity");
        try {
            roomService.addRoom(name, qty);
            ConsoleUI.printSuccess("Cinema room '" + name + "' added successfully!");
        } catch (IllegalArgumentException ex) {
            ConsoleUI.printError(ex.getMessage());
        }
        pause();
    }

    private void updateRoom() {
        ConsoleUI.printSectionHeader("UPDATE CINEMA ROOM");
        int id = readPositiveInt("Room ID to update");
        String name = readNonBlank("New Room Name");
        int qty = readPositiveInt("New Seat Quantity");
        try {
            roomService.updateRoom(id, name, qty);
            ConsoleUI.printSuccess("Cinema room id=" + id + " updated successfully!");
        } catch (IllegalArgumentException ex) {
            ConsoleUI.printError(ex.getMessage());
        }
        pause();
    }

    private void deleteRoom() {
        int id = readPositiveInt("Room ID to delete");
        ConsoleUI.printPrompt("Confirm delete room id=" + id + "? (yes/no)");
        String confirm = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(confirm)) {
            try {
                roomService.deleteRoom(id);
                ConsoleUI.printSuccess("Room id=" + id + " deleted.");
            } catch (Exception ex) {
                ConsoleUI.printError(ex.getMessage());
            }
        } else {
            ConsoleUI.printInfo("Delete cancelled.");
        }
        pause();
    }

    private void listAllDetails() {
        List<CinemaRoomDetail> details = roomDetailService.getAllDetails();
        ConsoleUI.printRoomDetailTable(details);
        pause();
    }

    private void findDetailById() {
        int id = readPositiveInt("Detail ID");
        roomDetailService.getDetailById(id).ifPresentOrElse(
                d -> {
                    ConsoleUI.printRoomDetailRecord(d);
                    pause();
                },
                () -> ConsoleUI.printError("Room detail with id=" + id + " not found."));
    }

    private void findDetailByRoomId() {
        int roomId = readPositiveInt("Room ID");
        roomDetailService.getDetailByRoomId(roomId).ifPresentOrElse(
                d -> {
                    ConsoleUI.printRoomDetailRecord(d);
                    pause();
                },
                () -> ConsoleUI.printError("No detail found for room id=" + roomId + "."));
    }

    private void addDetail() {
        ConsoleUI.printSectionHeader("ADD ROOM DETAIL");
        int roomId = readPositiveInt("Room ID");
        int rate = readPositiveInt("Room Rate");
        String dateStr = readDate("Active Date");
        ConsoleUI.printPrompt("Description");
        String desc = scanner.nextLine();
        try {
            roomDetailService.addDetail(roomId, rate, dateStr, desc);
            ConsoleUI.printSuccess("Room detail added for room id=" + roomId + "!");
        } catch (IllegalArgumentException ex) {
            ConsoleUI.printError(ex.getMessage());
        }
        pause();
    }

    private void updateDetail() {
        ConsoleUI.printSectionHeader("UPDATE ROOM DETAIL");
        int id = readPositiveInt("Detail ID to update");
        int rate = readPositiveInt("New Room Rate");
        String dateStr = readDate("New Active Date");
        ConsoleUI.printPrompt("New Description");
        String desc = scanner.nextLine();
        try {
            roomDetailService.updateDetail(id, rate, dateStr, desc);
            ConsoleUI.printSuccess("Room detail id=" + id + " updated!");
        } catch (IllegalArgumentException ex) {
            ConsoleUI.printError(ex.getMessage());
        }
        pause();
    }

    private void deleteDetail() {
        int id = readPositiveInt("Detail ID to delete");
        ConsoleUI.printPrompt("Confirm delete detail id=" + id + "? (yes/no)");
        String confirm = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(confirm)) {
            try {
                roomDetailService.deleteDetail(id);
                ConsoleUI.printSuccess("Room detail id=" + id + " deleted.");
            } catch (Exception ex) {
                ConsoleUI.printError(ex.getMessage());
            }
        } else {
            ConsoleUI.printInfo("Delete cancelled.");
        }
        pause();
    }

    private void listAllSeats() {
        List<Seat> seats = seatService.getAllSeats();
        ConsoleUI.printSeatTable(seats);
        pause();
    }

    private void findSeatById() {
        int id = readPositiveInt("Seat ID");
        seatService.getSeatById(id).ifPresentOrElse(
                s -> {
                    ConsoleUI.printSeatRecord(s);
                    pause();
                },
                () -> ConsoleUI.printError("Seat with id=" + id + " not found."));
    }

    private void findSeatsByRoomId() {
        int roomId = readPositiveInt("Room ID");
        List<Seat> seats = seatService.getSeatsByRoomId(roomId);
        ConsoleUI.printSeatTable(seats);
        pause();
    }

    private void findSeatsByStatus() {
        String status = readSeatStatus();
        List<Seat> seats = seatService.getSeatsByStatus(status);
        ConsoleUI.printSeatTable(seats);
        pause();
    }

    private void findSeatsByType() {
        String type = readSeatType();
        List<Seat> seats = seatService.getSeatsByType(type);
        ConsoleUI.printSeatTable(seats);
        pause();
    }

    private void addSeat() {
        ConsoleUI.printSectionHeader("ADD NEW SEAT");
        int roomId = readPositiveInt("Room ID");
        String col = readNonBlank("Seat Column (e.g. A, B, C)");
        int row = readPositiveInt("Seat Row");
        String status = readSeatStatus();
        String type = readSeatType();
        try {
            seatService.addSeat(roomId, col, row, status, type);
            ConsoleUI.printSuccess("Seat added successfully!");
        } catch (IllegalArgumentException ex) {
            ConsoleUI.printError(ex.getMessage());
        }
        pause();
    }

    private void updateSeat() {
        ConsoleUI.printSectionHeader("UPDATE SEAT");
        int seatId = readPositiveInt("Seat ID to update");
        int roomId = readPositiveInt("Room ID");
        String col = readNonBlank("New Seat Column");
        int row = readPositiveInt("New Seat Row");
        String status = readSeatStatus();
        String type = readSeatType();
        try {
            seatService.updateSeat(seatId, roomId, col, row, status, type);
            ConsoleUI.printSuccess("Seat id=" + seatId + " updated successfully!");
        } catch (IllegalArgumentException ex) {
            ConsoleUI.printError(ex.getMessage());
        }
        pause();
    }

    private void deleteSeat() {
        int id = readPositiveInt("Seat ID to delete");
        ConsoleUI.printPrompt("Confirm delete seat id=" + id + "? (yes/no)");
        String confirm = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(confirm)) {
            try {
                seatService.deleteSeat(id);
                ConsoleUI.printSuccess("Seat id=" + id + " deleted.");
            } catch (Exception ex) {
                ConsoleUI.printError(ex.getMessage());
            }
        } else {
            ConsoleUI.printInfo("Delete cancelled.");
        }
        pause();
    }

    private void pause() {
        System.out.println();
        ConsoleUI.printPrompt("Press ENTER to continue");
        scanner.nextLine();
    }
}
