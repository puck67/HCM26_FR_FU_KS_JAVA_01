package fa.training.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Validator {

    private static final List<String> VALID_SEAT_STATUSES = Arrays.asList("Available", "Not Available", "Booked");
    private static final List<String> VALID_SEAT_TYPES = Arrays.asList("VIP", "Normal");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Validator() {}

    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.trim();
    }

    public static String requireMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " must not exceed " + maxLength + " characters.");
        }
        return value;
    }

    public static int requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0.");
        }
        return value;
    }

    public static String requireSeatStatus(String status) {
        String trimmed = requireNonBlank(status, "Seat status");
        return VALID_SEAT_STATUSES.stream()
                .filter(s -> s.equalsIgnoreCase(trimmed))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Seat status must be one of: " + String.join(", ", VALID_SEAT_STATUSES)));
    }

    public static String requireSeatType(String type) {
        String trimmed = requireNonBlank(type, "Seat type");
        return VALID_SEAT_TYPES.stream()
                .filter(t -> t.equalsIgnoreCase(trimmed))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Seat type must be one of: " + String.join(", ", VALID_SEAT_TYPES)));
    }

    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Date must be in format yyyy-MM-dd. Got: " + dateStr);
        }
    }

    public static Optional<Integer> parseIntOptional(String input) {
        try {
            return Optional.of(Integer.parseInt(input.trim()));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
