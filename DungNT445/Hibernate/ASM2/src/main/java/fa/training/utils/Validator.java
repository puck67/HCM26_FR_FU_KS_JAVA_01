package fa.training.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Validator {

    /**
     * Checks if a string is not null and not empty after trimming.
     */
    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Checks if a string is a valid integer.
     */
    public static boolean isValidInteger(String input) {
        if (input == null) return false;
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string is a valid positive integer (greater than 0).
     */
    public static boolean isValidPositiveInteger(String input) {
        if (input == null) return false;
        try {
            int val = Integer.parseInt(input.trim());
            return val > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string conforms to the yyyy-MM-dd date format.
     */
    public static boolean isValidDate(String input) {
        if (input == null) return false;
        try {
            LocalDate.parse(input.trim());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if a string is a valid seat status ('Available', 'Not Available', 'Booked').
     */
    public static boolean isValidSeatStatus(String input) {
        if (input == null) return false;
        String trimmed = input.trim();
        return trimmed.equalsIgnoreCase("Available") ||
               trimmed.equalsIgnoreCase("Not Available") ||
               trimmed.equalsIgnoreCase("Booked");
    }

    /**
     * Checks if a string is a valid seat type ('VIP', 'Normal').
     */
    public static boolean isValidSeatType(String input) {
        if (input == null) return false;
        String trimmed = input.trim();
        return trimmed.equalsIgnoreCase("VIP") ||
               trimmed.equalsIgnoreCase("Normal");
    }
}
