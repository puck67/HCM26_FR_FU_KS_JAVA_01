package fa.training.utils;

import java.util.Scanner;

/**
 * Utility class to handle input validation safely (Defensive Programming).
 */
public class InputValidator {

    private static final Scanner scanner = new Scanner(System.in);

    public static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
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
            System.out.println("Input cannot be empty! Please try again.");
        }
    }

    public static String getString(String prompt, String regex, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches(regex)) {
                return input;
            }
            System.out.println(errorMsg);
        }
    }

    public static java.time.LocalDate getLocalDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return java.time.LocalDate.parse(input);
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Invalid date format! Please use YYYY-MM-DD (e.g. 2024-12-31).");
            }
        }
    }
}
