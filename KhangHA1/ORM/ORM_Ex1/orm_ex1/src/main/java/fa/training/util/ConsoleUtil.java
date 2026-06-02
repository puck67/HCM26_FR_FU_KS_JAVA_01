package fa.training.util;

import java.util.Scanner;

/**
 * Console I/O helpers — mirrors the LibraryManagement pattern.
 */
public class ConsoleUtil {

    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Input cannot be empty.");
        }
    }

    public static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    public static int readIntInRange(String message, int min, int max) {
        while (true) {
            int value = readInt(message);
            if (value >= min && value <= max) return value;
            System.out.println("Value must be between " + min + " and " + max + ".");
        }
    }

    public static boolean confirm(String message) {
        while (true) {
            System.out.print(message + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;
            System.out.println("Please enter Y or N.");
        }
    }

    public static void pressEnterToContinue() {
        System.out.println();
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}
