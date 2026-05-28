package fa.training.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Validator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean isValidSeatStatus(String status) {
        return "Available".equalsIgnoreCase(status)
                || "Not Available".equalsIgnoreCase(status)
                || "Booked".equalsIgnoreCase(status);
    }

    public static boolean isValidSeatType(String type) {
        return "VIP".equalsIgnoreCase(type)
                || "Normal".equalsIgnoreCase(type);
    }

    public static boolean isPositive(int val) {
        return val > 0;
    }

    public static LocalDate parseLocalDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String getValidSeatStatus(Scanner scanner) {
        while (true) {
            System.out.print("Enter Seat Status (Available, Not Available, Booked): ");
            String status = scanner.nextLine().trim();
            if ("Available".equalsIgnoreCase(status)) return "Available";
            if ("Not Available".equalsIgnoreCase(status)) return "Not Available";
            if ("Booked".equalsIgnoreCase(status)) return "Booked";
            System.out.println("Error: Seat Status must be one of: 'Available', 'Not Available', 'Booked'.");
        }
    }

    public static String getValidSeatType(Scanner scanner) {
        while (true) {
            System.out.print("Enter Seat Type (VIP, Normal): ");
            String type = scanner.nextLine().trim();
            if ("VIP".equalsIgnoreCase(type)) return "VIP";
            if ("Normal".equalsIgnoreCase(type)) return "Normal";
            System.out.println("Error: Seat Type must be one of: 'VIP', 'Normal'.");
        }
    }

    public static int getValidPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val > 0) {
                    return val;
                }
                System.out.println("Error: Must be a positive integer (> 0).");
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format. Please enter digits only.");
            }
        }
    }

    public static int getValidIntRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Error: Please enter a number between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format. Please enter digits only.");
            }
        }
    }

    public static String getNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Error: Input cannot be empty.");
        }
    }

    public static LocalDate getValidDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            String input = scanner.nextLine().trim();
            LocalDate date = parseLocalDate(input);
            if (date != null) {
                return date;
            }
            System.out.println("Error: Invalid date format. Please write in yyyy-MM-dd format (e.g. 2026-05-28).");
        }
    }
}
