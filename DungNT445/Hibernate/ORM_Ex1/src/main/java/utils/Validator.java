package utils;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Utility class providing static methods to validate student attributes.
 */
public class Validator {

    // Regex to allow letters (including unicode/Vietnamese characters) and spaces
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s\\p{L}]+$");

    /**
     * Validates student's full name.
     * Rules: Not null, not blank, length <= 50, contains only alphabetic characters and spaces.
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("[VALIDATION ERROR] Tên không được để trống hoặc chỉ chứa khoảng trắng.");
            return false;
        }
        String trimmed = name.trim();
        if (trimmed.length() > 50) {
            System.err.println("[VALIDATION ERROR] Tên không được vượt quá 50 ký tự (Hiện tại: " + trimmed.length() + ").");
            return false;
        }
        if (!NAME_PATTERN.matcher(trimmed).matches()) {
            System.err.println("[VALIDATION ERROR] Tên chỉ được phép chứa chữ cái và khoảng trắng.");
            return false;
        }
        return true;
    }

    /**
     * Validates student's age.
     * Rules: Not null, must be between 1 and 120 (inclusive).
     */
    public static boolean isValidAge(Integer age) {
        if (age == null) {
            System.err.println("[VALIDATION ERROR] Tuổi không được để trống.");
            return false;
        }
        if (age < 1 || age > 120) {
            System.err.println("[VALIDATION ERROR] Tuổi phải nằm trong khoảng từ 1 đến 120.");
            return false;
        }
        return true;
    }

    /**
     * Validates student's birth date.
     * Rules: Not null, must not be a future date.
     */
    public static boolean isValidBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            System.err.println("[VALIDATION ERROR] Ngày sinh không được để trống.");
            return false;
        }
        if (birthDate.isAfter(LocalDate.now())) {
            System.err.println("[VALIDATION ERROR] Ngày sinh không được là một ngày trong tương lai.");
            return false;
        }
        return true;
    }

    /**
     * Validates course's title.
     */
    public static boolean isValidCourseTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.err.println("[VALIDATION ERROR] Tên khóa học không được để trống hoặc chỉ chứa khoảng trắng.");
            return false;
        }
        String trimmed = title.trim();
        if (trimmed.length() > 255) {
            System.err.println("[VALIDATION ERROR] Tên khóa học không được vượt quá 255 ký tự (Hiện tại: " + trimmed.length() + ").");
            return false;
        }
        return true;
    }

    /**
     * Validates course's credit.
     */
    public static boolean isValidCourseCredit(Integer credit) {
        if (credit == null) {
            System.err.println("[VALIDATION ERROR] Số tín chỉ không được để trống.");
            return false;
        }
        if (credit < 1 || credit > 20) {
            System.err.println("[VALIDATION ERROR] Số tín chỉ phải nằm trong khoảng từ 1 đến 20.");
            return false;
        }
        return true;
    }
}
