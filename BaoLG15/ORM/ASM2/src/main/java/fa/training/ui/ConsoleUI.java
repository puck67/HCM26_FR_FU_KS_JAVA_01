package fa.training.ui;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;

import java.util.List;

import static fa.training.ui.ConsoleColors.*;

public final class ConsoleUI {

    private static final int BOX_WIDTH = 52;

    private ConsoleUI() {
    }

    public static void printMainBanner() {
        System.out.println();
        printTopBorder();
        printCenteredTitle("MOVIE THEATER SYSTEM");
        printDivider();
        printMenuItem("1", "Cinema Room Management");
        printMenuItem("2", "Room Detail Management");
        printMenuItem("3", "Seat Management");
        printMenuItem("0", "Exit");
        printBottomBorder();
    }

    public static void printRoomMenu() {
        System.out.println();
        printTopBorder();
        printCenteredTitle("CINEMA ROOM MANAGEMENT");
        printDivider();
        printMenuItem("1", "List all rooms");
        printMenuItem("2", "Find room by ID");
        printMenuItem("3", "Add new room");
        printMenuItem("4", "Update room");
        printMenuItem("5", "Delete room");
        printMenuItem("0", "Back to main menu");
        printBottomBorder();
    }

    public static void printDetailMenu() {
        System.out.println();
        printTopBorder();
        printCenteredTitle("ROOM DETAIL MANAGEMENT");
        printDivider();
        printMenuItem("1", "List all room details");
        printMenuItem("2", "Find detail by ID");
        printMenuItem("3", "Find detail by Room ID");
        printMenuItem("4", "Add room detail");
        printMenuItem("5", "Update room detail");
        printMenuItem("6", "Delete room detail");
        printMenuItem("0", "Back to main menu");
        printBottomBorder();
    }

    public static void printSeatMenu() {
        System.out.println();
        printTopBorder();
        printCenteredTitle("SEAT MANAGEMENT");
        printDivider();
        printMenuItem("1", "List all seats");
        printMenuItem("2", "Find seat by ID");
        printMenuItem("3", "Find seats by Room ID");
        printMenuItem("4", "Find seats by status");
        printMenuItem("5", "Find seats by type");
        printMenuItem("6", "Add new seat");
        printMenuItem("7", "Update seat");
        printMenuItem("8", "Delete seat");
        printMenuItem("0", "Back to main menu");
        printBottomBorder();
    }

    private static void printTopBorder() {
        System.out.println(CYAN + BOLD + "╔" + "═".repeat(BOX_WIDTH) + "╗" + RESET);
    }

    private static void printBottomBorder() {
        System.out.println(CYAN + BOLD + "╚" + "═".repeat(BOX_WIDTH) + "╝" + RESET);
    }

    private static void printDivider() {
        System.out.println(CYAN + BOLD + "╠" + "═".repeat(BOX_WIDTH) + "╣" + RESET);
    }

    private static void printCenteredTitle(String title) {
        int visibleLen = title.length();
        int totalPad = BOX_WIDTH - visibleLen;
        int leftPad = totalPad / 2;
        int rightPad = totalPad - leftPad;
        String padded = " ".repeat(Math.max(0, leftPad)) + title + " ".repeat(Math.max(0, rightPad));
        System.out.println(CYAN + BOLD + "║" + RESET + YELLOW + BOLD + padded + RESET + CYAN + BOLD + "║" + RESET);
    }

    private static void printMenuItem(String key, String label) {
        String visiblePrefix = "  [" + key + "]  ";
        int visibleLen = visiblePrefix.length() + label.length();
        int padding = Math.max(0, BOX_WIDTH - visibleLen);
        String content = "  " + BOLD + CYAN + "[" + key + "]" + RESET
                + "  " + WHITE + label + RESET
                + " ".repeat(padding);
        System.out.println(CYAN + BOLD + "║" + RESET + content + CYAN + BOLD + "║" + RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(GREEN + BOLD + "  ✔  " + RESET + GREEN + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + BOLD + "  ✘  " + RESET + RED + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println(CYAN + "  ℹ  " + RESET + message);
    }

    public static void printPrompt(String label) {
        System.out.print(YELLOW + "  » " + label + ": " + RESET);
    }

    public static void printSectionHeader(String title) {
        System.out.println();
        int dashes = Math.max(0, 46 - title.length());
        System.out.println(BOLD + BLUE + "  ── " + title + " " + "─".repeat(dashes) + RESET);
    }

    public static void printRoomTable(List<CinemaRoom> rooms) {
        if (rooms.isEmpty()) {
            printInfo("No cinema rooms found.");
            return;
        }
        printSectionHeader("CINEMA ROOMS");
        System.out.printf(BOLD + "  %-6s %-30s %-10s%n" + RESET, "ID", "NAME", "SEAT QTY");
        System.out.println("  " + "─".repeat(48));
        rooms.forEach(r -> System.out.printf("  %-6d %-30s %-10d%n",
                r.getCinemaRoomId(), r.getCinemaRoomName(), r.getSeatQuantity()));
    }

    public static void printRoomDetail(CinemaRoom room) {
        printSectionHeader("ROOM DETAIL");
        System.out.printf("  %-14s : %d%n", "ID", room.getCinemaRoomId());
        System.out.printf("  %-14s : %s%n", "Name", room.getCinemaRoomName());
        System.out.printf("  %-14s : %d%n", "Seat Qty", room.getSeatQuantity());
    }

    public static void printRoomDetailTable(List<CinemaRoomDetail> details) {
        if (details.isEmpty()) {
            printInfo("No room details found.");
            return;
        }
        printSectionHeader("ROOM DETAILS");
        System.out.printf(BOLD + "  %-6s %-8s %-8s %-14s %-20s%n" + RESET,
                "ID", "ROOM_ID", "RATE", "ACTIVE_DATE", "DESCRIPTION");
        System.out.println("  " + "─".repeat(60));
        details.forEach(d -> System.out.printf("  %-6d %-8d %-8d %-14s %-20s%n",
                d.getCinemaRoomDetailId(),
                d.getCinemaRoom() != null ? d.getCinemaRoom().getCinemaRoomId() : 0,
                d.getRoomRate(), d.getActiveDate(), d.getRoomDescription()));
    }

    public static void printRoomDetailRecord(CinemaRoomDetail d) {
        printSectionHeader("ROOM DETAIL RECORD");
        System.out.printf("  %-14s : %d%n", "Detail ID", d.getCinemaRoomDetailId());
        System.out.printf("  %-14s : %d%n", "Room ID",
                d.getCinemaRoom() != null ? d.getCinemaRoom().getCinemaRoomId() : 0);
        System.out.printf("  %-14s : %d%n", "Room Rate", d.getRoomRate());
        System.out.printf("  %-14s : %s%n", "Active Date", d.getActiveDate());
        System.out.printf("  %-14s : %s%n", "Description", d.getRoomDescription());
    }

    public static void printSeatTable(List<Seat> seats) {
        if (seats.isEmpty()) {
            printInfo("No seats found.");
            return;
        }
        printSectionHeader("SEATS");
        System.out.printf(BOLD + "  %-6s %-8s %-6s %-5s %-16s %-8s%n" + RESET,
                "ID", "ROOM_ID", "COL", "ROW", "STATUS", "TYPE");
        System.out.println("  " + "─".repeat(52));
        seats.forEach(s -> System.out.printf("  %-6d %-8d %-6s %-5d %-16s %-8s%n",
                s.getSeatId(),
                s.getCinemaRoom() != null ? s.getCinemaRoom().getCinemaRoomId() : 0,
                s.getSeatColumn(), s.getSeatRow(),
                s.getSeatStatus(), s.getSeatType()));
    }

    public static void printSeatRecord(Seat s) {
        printSectionHeader("SEAT RECORD");
        System.out.printf("  %-14s : %d%n", "Seat ID", s.getSeatId());
        System.out.printf("  %-14s : %d%n", "Room ID",
                s.getCinemaRoom() != null ? s.getCinemaRoom().getCinemaRoomId() : 0);
        System.out.printf("  %-14s : %s%n", "Column", s.getSeatColumn());
        System.out.printf("  %-14s : %d%n", "Row", s.getSeatRow());
        System.out.printf("  %-14s : %s%n", "Status", s.getSeatStatus());
        System.out.printf("  %-14s : %s%n", "Type", s.getSeatType());
    }
}
