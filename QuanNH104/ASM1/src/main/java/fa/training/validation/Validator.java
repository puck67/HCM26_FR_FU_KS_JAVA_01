package fa.training.validation;

import java.util.regex.Pattern;

public class Validator {
    // Biểu thức chính quy cho phép tất cả các chữ cái (bao gồm chữ có dấu Tiếng Việt) và khoảng trắng
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}\\s]+$");

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && NAME_PATTERN.matcher(name.trim()).matches() && name.trim().length() <= 50;
    }

    public static void validateEmployee(String firstName, String lastName) {
        if (!isValidName(firstName)) {
            throw new IllegalArgumentException("First name cannot be empty, must contain only letters and spaces, and must be under 50 characters.");
        }
        if (!isValidName(lastName)) {
            throw new IllegalArgumentException("Last name cannot be empty, must contain only letters and spaces, and must be under 50 characters.");
        }
    }
}
