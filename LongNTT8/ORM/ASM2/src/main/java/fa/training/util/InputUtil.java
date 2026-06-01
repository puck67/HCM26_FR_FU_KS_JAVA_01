package fa.training.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static String getString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public static LocalDate getLocalDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            try {
                return LocalDate.parse(scanner.nextLine().trim(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }

    public static String getSeatStatus(String prompt) {
        while (true) {
            System.out.print(prompt + " (A=Available, B=Booked): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if ("A".equals(input) || "AVAILABLE".equals(input)) return "Available";
            if ("B".equals(input) || "BOOKED".equals(input)) return "Booked";
            System.out.println("Invalid status. Please enter A or B.");
        }
    }

    public static String getSeatType(String prompt) {
        while (true) {
            System.out.print(prompt + " (V=VIP, N=Normal): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if ("V".equals(input) || "VIP".equals(input)) return "VIP";
            if ("N".equals(input) || "NORMAL".equals(input)) return "Normal";
            System.out.println("Invalid type. Please enter V or N.");
        }
    }
}
