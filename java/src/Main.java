import db.DatabaseManager;
import service.HotelRoomService;
import util.ConsoleHelper;

import java.util.Optional;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        DatabaseManager.getInstance().init();

        Scanner sc = new Scanner(System.in);
        HotelRoomService service = new HotelRoomService();

        printWelcomeBanner();

        boolean running = true;
        while (running) {
            ConsoleHelper.printMenu();
            running = handleChoice(sc, service);
            if (running) pauseForUser(sc);
        }

        sc.close();
        DatabaseManager.getInstance().close();
    }

    /** Đọc lựa chọn và dispatch — trả false khi user chọn Exit. */
    private static boolean handleChoice(Scanner sc, HotelRoomService service) {
        return parseChoice(sc)
            .map(choice -> dispatch(choice, sc, service))
            .orElseGet(() -> {
                ConsoleHelper.printError("Invalid input — please enter a number (0-6).");
                return true;
            });
    }

    /** Parse input thành int, trả Optional.empty() nếu không phải số. */
    private static Optional<Integer> parseChoice(Scanner sc) {
        try {
            return Optional.of(Integer.parseInt(sc.nextLine().trim()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Dispatch theo choice.
     * @return true = tiếp tục chạy, false = thoát.
     */
    private static boolean dispatch(int choice, Scanner sc, HotelRoomService service) {
        System.out.println();
        switch (choice) {
            case 1 -> service.addRoom(sc);
            case 2 -> service.displayAll();
            case 3 -> service.updateRoom(sc);
            case 4 -> service.deleteRoom(sc);
            case 5 -> service.searchRoom(sc);
            case 6 -> service.sortRooms(sc);
            case 0 -> { printGoodbye(); return false; }
            default -> ConsoleHelper.printError("Invalid choice. Please enter 0 – 6.");
        }
        return true;
    }


    private static void printWelcomeBanner() {
        String banner = new StringBuilder()
            .append("\n")
            .append(ConsoleHelper.CYAN).append(ConsoleHelper.BOLD)
            .append("  ╔══════════════════════════════════════════════════╗\n")
            .append("  ║       WELCOME TO HOTEL ROOM MANAGEMENT SYSTEM    ║\n")
            .append("  ╚══════════════════════════════════════════════════╝")
            .append(ConsoleHelper.RESET)
            .append("\n")
            .toString();
        System.out.println(banner);
    }

    private static void printGoodbye() {
        ConsoleHelper.printInfo("Thank you for using Hotel Room Management System. Goodbye!");
    }

    private static void pauseForUser(Scanner sc) {
        System.out.println();
        System.out.print("Press ENTER to continue...");
        sc.nextLine();
    }
}
