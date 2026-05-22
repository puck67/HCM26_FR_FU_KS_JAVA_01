package util;

import model.HotelRoom;

import java.util.List;

public class ConsoleHelper {

    public static final String RESET  = "\033[0m";
    public static final String BOLD   = "\033[1m";
    public static final String CYAN   = "\033[36m";
    public static final String GREEN  = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String RED    = "\033[31m";
    public static final String BLUE   = "\033[34m";

    public static void printSuccess(String msg) {
        System.out.println(GREEN + "[SUCCESS] " + msg + RESET);
    }

    public static void printError(String msg) {
        System.out.println(RED + "[ERROR] " + msg + RESET);
    }

    public static void printInfo(String msg) {
        System.out.println(YELLOW + "[INFO] " + msg + RESET);
    }

    public static void printHeader(String title) {
        String line = buildLine("=", 90);
        System.out.println(CYAN + line + RESET);
        System.out.println(CYAN + BOLD + centerText(title, 90) + RESET);
        System.out.println(CYAN + line + RESET);
    }

    public static void printTableHeader() {
        System.out.println(BLUE + buildLine("-", 90) + RESET);
        System.out.printf(BOLD + "| %-8s | %-10s | %12s | %8s | %-12s | %-30s |%n" + RESET,
            "Room ID", "Type", "Price/Night", "Capacity", "Status", "Description");
        System.out.println(BLUE + buildLine("-", 90) + RESET);
    }

    public static void printTableFooter() {
        System.out.println(BLUE + buildLine("-", 90) + RESET);
    }

    public static void printRoomTable(List<HotelRoom> rooms) {
        printTableHeader();
        rooms.stream().map(HotelRoom::toString).forEach(System.out::println);
        printTableFooter();
        System.out.printf("  Total: %d room(s)%n", rooms.size());
    }

    public static void printMenu() {
        System.out.println();
        String[] lines = {
            "╔══════════════════════════════════╗",
            "║    HOTEL ROOM MANAGEMENT SYSTEM  ║",
            "╠══════════════════════════════════╣",
            "║  1.Add New Room                  ║",
            "║  2.Display All Rooms             ║",
            "║  3.Update a Room                 ║",
            "║  4.Delete a Room                 ║",
            "║  5.Search Room                   ║",
            "║  6.Sort Rooms                    ║",
            "║  0.Exit                          ║",
            "╚══════════════════════════════════╝"
        };
        java.util.Arrays.stream(lines)
             .map(l -> CYAN + l + RESET)
             .forEach(System.out::println);

        System.out.print(BOLD + "Enter your choice: " + RESET);
    }

    private static String buildLine(String ch, int width) {
        StringBuilder sb = new StringBuilder(width);
        for (int i = 0; i < width; i++) sb.append(ch);
        return sb.toString();
    }

    private static String centerText(String text, int width) {
        int padding = Math.max(0, (width - text.length()) / 2);
        StringBuilder sb = new StringBuilder(width);
        for (int i = 0; i < padding; i++) sb.append(' ');
        sb.append(text);
        return sb.toString();
    }
}
