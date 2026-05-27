package service;

import db.HotelRoomDao;
import model.HotelRoom;
import model.RoomStatus;
import model.RoomType;
import util.ConsoleHelper;
import util.Validator;

import java.sql.SQLException;
import java.util.*;

public class HotelRoomService {

    private static final int MAX_ATTEMPTS = 3;
    private final HotelRoomDao dao = new HotelRoomDao();

    public void addRoom(Scanner sc) {
        ConsoleHelper.printHeader("ADD NEW HOTEL ROOM");
        try {
            String generatedId = dao.generateNextRoomId();
            ConsoleHelper.printInfo("Auto-generated Room ID: " + generatedId);
            
            HotelRoom room = new HotelRoom(
                generatedId,
                promptRoomType(sc),
                promptPrice(sc),
                promptCapacity(sc),
                promptStatus(sc),
                promptDescription(sc)
            );

            if (!Validator.validateHotelRoom(room)) {
                ConsoleHelper.printError("Invalid room data!");
                return;
            }

            dao.add(room);
            ConsoleHelper.printSuccess("Room " + room.getRoomId() + " added successfully!");
        } catch (SQLException e) {
            ConsoleHelper.printError("Failed to add room: " + e.getMessage());
        }
    }

    public void displayAll() {
        ConsoleHelper.printHeader("ALL HOTEL ROOMS");
        try {
            List<HotelRoom> rooms = dao.getAll();
            if (rooms.isEmpty()) { ConsoleHelper.printInfo("No rooms found."); return; }
            ConsoleHelper.printRoomTable(rooms);
        } catch (SQLException e) {
            ConsoleHelper.printError("Failed to load rooms: " + e.getMessage());
        }
    }

    public void updateRoom(Scanner sc) {
        ConsoleHelper.printHeader("UPDATE HOTEL ROOM");
        findByIdFromInput(sc, "update").ifPresent(room -> applyUpdates(sc, room));
    }

    private void applyUpdates(Scanner sc, HotelRoom room) {
        printRoomPreview(room);
        System.out.println("(Press ENTER to keep the current value)\n");

        updateRoomType(sc, room);
        updatePrice(sc, room);
        updateCapacity(sc, room);
        updateStatus(sc, room);
        updateDescription(sc, room);

        if (!Validator.validateHotelRoom(room)) {
            ConsoleHelper.printError("Invalid room data!");
            return;
        }

        try {
            dao.update(room);
            ConsoleHelper.printSuccess("Room " + room.getRoomId() + " updated successfully!");
        } catch (SQLException e) {
            ConsoleHelper.printError("Failed to update room: " + e.getMessage());
        }
    }

    private void updateRoomType(Scanner sc, HotelRoom room) {
        System.out.printf("Room Type [%s] (%s): ", room.getRoomType().name(), RoomType.allNames());
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (Validator.isValidRoomType(input)) {
            RoomType.fromString(input).ifPresent(room::setRoomType);
        } else {
            ConsoleHelper.printInfo(Validator.roomTypeError() + " — kept original.");
        }
    }

    private void updatePrice(Scanner sc, HotelRoom room) {
        System.out.printf("Price per Night [%.2f]: ", room.getPricePerNight());
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (Validator.isValidPriceString(input)) {
            room.setPricePerNight(Double.parseDouble(input));
        } else {
            ConsoleHelper.printInfo(Validator.priceError() + " — kept original.");
        }
    }

    private void updateCapacity(Scanner sc, HotelRoom room) {
        System.out.printf("Capacity [%d]: ", room.getCapacity());
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (Validator.isValidCapacityString(input)) {
            room.setCapacity(Integer.parseInt(input));
        } else {
            ConsoleHelper.printInfo(Validator.capacityError() + " — kept original.");
        }
    }

    private void updateStatus(Scanner sc, HotelRoom room) {
        System.out.printf("Status [%s] (%s): ", room.getStatus().name(), RoomStatus.allNames());
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (Validator.isValidStatus(input)) {
            RoomStatus.fromString(input).ifPresent(room::setStatus);
        } else {
            ConsoleHelper.printInfo(Validator.statusError() + " — kept original.");
        }
    }

    private void updateDescription(Scanner sc, HotelRoom room) {
        System.out.printf("Description [%s]: ", room.getDescription());
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (Validator.isValidDescription(input)) {
            room.setDescription(input);
        } else {
            ConsoleHelper.printInfo(Validator.descriptionError() + " — kept original.");
        }
    }

    public void deleteRoom(Scanner sc) {
        ConsoleHelper.printHeader("DELETE HOTEL ROOM");
        findByIdFromInput(sc, "delete").ifPresent(room -> confirmAndDelete(sc, room));
    }

    private void confirmAndDelete(Scanner sc, HotelRoom room) {
        printRoomPreview(room);
        if (isConfirmed(sc)) {
            try {
                dao.delete(room.getRoomId());
                ConsoleHelper.printSuccess("Room " + room.getRoomId() + " deleted.");
            } catch (SQLException e) {
                ConsoleHelper.printError("Failed to delete room: " + e.getMessage());
            }
        } else {
            ConsoleHelper.printInfo("Delete cancelled.");
        }
    }

    private boolean isConfirmed(Scanner sc) {
        System.out.print("Are you sure? (yes/no): ");
        String answer = sc.nextLine().trim();
        return answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y");
    }

    public void searchRoom(Scanner sc) {
        ConsoleHelper.printHeader("SEARCH HOTEL ROOM");
        printSearchMenu();
        List<HotelRoom> results = collectSearchResults(sc);
        printSearchResults(results);
    }

    private void printSearchMenu() {
        System.out.println("  1. Search by Room ID");
        System.out.println("  2. Search by Room Type");
        System.out.println("  3. Search by Status");
        System.out.print("Choose search option: ");
    }

    private List<HotelRoom> collectSearchResults(Scanner sc) {
        String opt = sc.nextLine().trim();
        try {
            return switch (opt) {
                case "1" -> searchById(sc);
                case "2" -> searchByType(sc);
                case "3" -> searchByStatus(sc);
                default  -> {
                    ConsoleHelper.printError("Invalid option.");
                    yield Collections.emptyList();
                }
            };
        } catch (SQLException e) {
            ConsoleHelper.printError("Search failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<HotelRoom> searchById(Scanner sc) throws SQLException {
        System.out.print("Enter Room ID: ");
        String id = sc.nextLine().trim().toUpperCase();
        if (!Validator.isValidRoomId(id)) {
            ConsoleHelper.printError(Validator.roomIdError());
            return Collections.emptyList();
        }
        return dao.findById(id).map(List::of).orElse(Collections.emptyList());
    }

    private List<HotelRoom> searchByType(Scanner sc) throws SQLException {
        System.out.printf("Enter Room Type (%s): ", RoomType.allNames());
        String input = sc.nextLine().trim();
        if (!Validator.isValidRoomType(input)) {
            ConsoleHelper.printError(Validator.roomTypeError());
            return Collections.emptyList();
        }
        return dao.findByType(input.toUpperCase());
    }

    private List<HotelRoom> searchByStatus(Scanner sc) throws SQLException {
        System.out.printf("Enter Status (%s): ", RoomStatus.allNames());
        String input = sc.nextLine().trim();
        if (!Validator.isValidStatus(input)) {
            ConsoleHelper.printError(Validator.statusError());
            return Collections.emptyList();
        }
        return dao.findByStatus(input.toUpperCase());
    }

    private void printSearchResults(List<HotelRoom> results) {
        if (results.isEmpty()) { ConsoleHelper.printInfo("No matching rooms found."); return; }
        ConsoleHelper.printRoomTable(results);
    }

    public void sortRooms(Scanner sc) {
        ConsoleHelper.printHeader("SORT ROOMS");
        printSortMenu();
        String opt = sc.nextLine().trim();
        try {
            List<HotelRoom> rooms = new ArrayList<>(dao.getAll());
            chooseComparator(opt).ifPresentOrElse(
                cmp -> {
                    rooms.sort(cmp);
                    ConsoleHelper.printSuccess("Rooms sorted.");
                    ConsoleHelper.printRoomTable(rooms);
                },
                () -> ConsoleHelper.printError("Invalid option.")
            );
        } catch (SQLException e) {
            ConsoleHelper.printError("Failed to load rooms: " + e.getMessage());
        }
    }

    private void printSortMenu() {
        System.out.println("  1. Sort by Room ID   (A → Z)");
        System.out.println("  2. Sort by Price      (Low → High)");
        System.out.println("  3. Sort by Price      (High → Low)");
        System.out.println("  4. Sort by Room Type  (A → Z)");
        System.out.print("Choose sort option: ");
    }

    private Optional<Comparator<HotelRoom>> chooseComparator(String opt) {
        Map<String, Comparator<HotelRoom>> map = new LinkedHashMap<>();
        map.put("1", Comparator.comparing(HotelRoom::getRoomId));
        map.put("2", Comparator.comparingDouble(HotelRoom::getPricePerNight));
        map.put("3", Comparator.comparingDouble(HotelRoom::getPricePerNight).reversed());
        map.put("4", Comparator.comparing(r -> r.getRoomType().name()));
        return Optional.ofNullable(map.get(opt));
    }

    private Optional<String> promptRoomId(Scanner sc, boolean mustBeNew) {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            System.out.print("Room ID (format R###, e.g. R001): ");
            String id = sc.nextLine().trim().toUpperCase();

            if (!Validator.isValidRoomId(id)) {
                ConsoleHelper.printError(Validator.roomIdError());
                continue;
            }
            try {
                if (mustBeNew && dao.existsById(id)) {
                    ConsoleHelper.printError("Room ID '" + id + "' already exists in database.");
                } else {
                    return Optional.of(id);
                }
            } catch (SQLException e) {
                ConsoleHelper.printError("DB error: " + e.getMessage());
                return Optional.empty();
            }
        }
        ConsoleHelper.printError("Too many invalid attempts. Returning to menu.");
        return Optional.empty();
    }

    private RoomType promptRoomType(Scanner sc) {
        while (true) {
            System.out.printf("Room Type (%s): ", RoomType.allNames());
            String input = sc.nextLine().trim();
            if (Validator.isValidRoomType(input))
                return RoomType.fromString(input).orElseThrow();
            ConsoleHelper.printError(Validator.roomTypeError());
        }
    }

    private double promptPrice(Scanner sc) {
        while (true) {
            System.out.print("Price per Night (e.g. 99.99): ");
            String input = sc.nextLine().trim();
            if (Validator.isValidPriceString(input)) return Double.parseDouble(input);
            ConsoleHelper.printError(Validator.priceError());
        }
    }

    private int promptCapacity(Scanner sc) {
        while (true) {
            System.out.print("Capacity (1 – 10): ");
            String input = sc.nextLine().trim();
            if (Validator.isValidCapacityString(input)) return Integer.parseInt(input);
            ConsoleHelper.printError(Validator.capacityError());
        }
    }

    private RoomStatus promptStatus(Scanner sc) {
        while (true) {
            System.out.printf("Status (%s): ", RoomStatus.allNames());
            String input = sc.nextLine().trim();
            if (Validator.isValidStatus(input))
                return RoomStatus.fromString(input).orElseThrow();
            ConsoleHelper.printError(Validator.statusError());
        }
    }

    private String promptDescription(Scanner sc) {
        while (true) {
            System.out.print("Description (3–100 chars): ");
            String value = sc.nextLine().trim();
            if (Validator.isValidDescription(value)) return value;
            ConsoleHelper.printError(Validator.descriptionError());
        }
    }

    private Optional<HotelRoom> findByIdFromInput(Scanner sc, String action) {
        System.out.printf("Enter Room ID to %s: ", action);
        String id = sc.nextLine().trim().toUpperCase();
        try {
            Optional<HotelRoom> room = dao.findById(id);
            if (room.isEmpty()) ConsoleHelper.printError("Room '" + id + "' not found.");
            return room;
        } catch (SQLException e) {
            ConsoleHelper.printError("DB error: " + e.getMessage());
            return Optional.empty();
        }
    }

    private void printRoomPreview(HotelRoom room) {
        ConsoleHelper.printTableHeader();
        System.out.println(room);
        ConsoleHelper.printTableFooter();
    }
}
