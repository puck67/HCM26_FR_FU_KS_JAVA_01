package fa.training.util;

import java.time.Year;
import java.util.regex.Pattern;

public final class Validator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\d{7,15}$");

    private Validator() {
    }

    public static boolean isValidId(String id) {
        return id != null
                && !id.isBlank()
                && id.length() <= 10
                && id.matches("^[a-zA-Z0-9]+$");
    }

    public static boolean isValidTitle(String title) {
        return title != null && !title.isBlank();
    }

    public static boolean isValidName(String name) {
        return name != null && !name.isBlank();
    }

    public static boolean isValidRating(double rating) {
        return rating >= 0.0 && rating <= 10.0;
    }

    public static boolean isValidReleaseYear(int year) {
        int maxYear = Year.now().getValue() + 5;
        return year >= 1888 && year <= maxYear;
    }

    public static boolean isPositiveInteger(String value) {
        if (value == null || value.isBlank()) return false;
        try {
            return Integer.parseInt(value.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDouble(String value) {
        if (value == null || value.isBlank()) return false;
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }
}
