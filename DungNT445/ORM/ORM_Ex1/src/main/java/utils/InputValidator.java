package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utility class to read and validate console inputs for Students.
 */
public class InputValidator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Reads a valid name from console.
     */
    public static String getValidName(Scanner scanner) {
        while (true) {
            System.out.print("Nhập họ và tên sinh viên: ");
            String input = scanner.nextLine();
            if (Validator.isValidName(input)) {
                return input.trim();
            }
        }
    }

    /**
     * Reads a valid age from console.
     */
    public static int getValidAge(Scanner scanner) {
        while (true) {
            System.out.print("Nhập tuổi sinh viên: ");
            String input = scanner.nextLine().trim();
            try {
                int age = Integer.parseInt(input);
                if (Validator.isValidAge(age)) {
                    return age;
                }
            } catch (NumberFormatException e) {
                System.err.println("[INPUT ERROR] Tuổi phải là một số nguyên hợp lệ.");
            }
        }
    }

    /**
     * Reads a valid birth date from console.
     */
    public static LocalDate getValidBirthDate(Scanner scanner) {
        while (true) {
            System.out.print("Nhập ngày sinh (yyyy-MM-dd): ");
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
                if (Validator.isValidBirthDate(date)) {
                    return date;
                }
            } catch (DateTimeParseException e) {
                System.err.println("[INPUT ERROR] Định dạng ngày không hợp lệ. Vui lòng dùng định dạng yyyy-MM-dd (ví dụ: 2005-12-31).");
            }
        }
    }

    /**
     * Reads a valid integer option from a range.
     */
    public static int getValidOption(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) {
                    return val;
                }
                System.err.println("[INPUT ERROR] Lựa chọn nằm ngoài phạm vi cho phép [" + min + " - " + max + "].");
            } catch (NumberFormatException e) {
                System.err.println("[INPUT ERROR] Lựa chọn phải là một số nguyên.");
            }
        }
    }

    /**
     * Reads an ID from console.
     */
    public static int getValidId(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int id = Integer.parseInt(input);
                if (id > 0) {
                    return id;
                }
                System.err.println("[INPUT ERROR] ID phải là số nguyên dương lớn hơn 0.");
            } catch (NumberFormatException e) {
                System.err.println("[INPUT ERROR] ID phải là một số nguyên.");
            }
        }
    }

    // --- UPDATE HELPER METHODS (allows pressing Enter to keep current value) ---

    /**
     * Reads a name for update. Pressing Enter keeps the current value.
     */
    public static String getUpdateName(Scanner scanner, String currentName) {
        while (true) {
            System.out.printf("Nhập tên mới (Ấn Enter để GIỮ '%s'): ", currentName);
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                return currentName;
            }
            if (Validator.isValidName(input)) {
                return input.trim();
            }
        }
    }

    /**
     * Reads an age for update. Pressing Enter keeps the current value.
     */
    public static int getUpdateAge(Scanner scanner, int currentAge) {
        while (true) {
            System.out.printf("Nhập tuổi mới (Ấn Enter để GIỮ '%d'): ", currentAge);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentAge;
            }
            try {
                int age = Integer.parseInt(input);
                if (Validator.isValidAge(age)) {
                    return age;
                }
            } catch (NumberFormatException e) {
                System.err.println("[INPUT ERROR] Tuổi phải là một số nguyên hợp lệ.");
            }
        }
    }

    /**
     * Reads a birth date for update. Pressing Enter keeps the current value.
     */
    public static LocalDate getUpdateBirthDate(Scanner scanner, LocalDate currentBirthDate) {
        String formattedCurrent = (currentBirthDate != null) ? currentBirthDate.format(DATE_FORMATTER) : "N/A";
        while (true) {
            System.out.printf("Nhập ngày sinh mới (yyyy-MM-dd) (Ấn Enter để GIỮ '%s'): ", formattedCurrent);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentBirthDate;
            }
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
                if (Validator.isValidBirthDate(date)) {
                    return date;
                }
            } catch (DateTimeParseException e) {
                System.err.println("[INPUT ERROR] Định dạng ngày không hợp lệ. Vui lòng dùng định dạng yyyy-MM-dd.");
            }
        }
    }

    /**
     * Reads a valid course title from console.
     */
    public static String getValidCourseTitle(Scanner scanner) {
        while (true) {
            System.out.print("Nhập tên khóa học: ");
            String input = scanner.nextLine();
            if (Validator.isValidCourseTitle(input)) {
                return input.trim();
            }
        }
    }

    /**
     * Reads a valid course credit from console.
     */
    public static int getValidCourseCredit(Scanner scanner) {
        while (true) {
            System.out.print("Nhập số tín chỉ: ");
            String input = scanner.nextLine().trim();
            try {
                int credit = Integer.parseInt(input);
                if (Validator.isValidCourseCredit(credit)) {
                    return credit;
                }
            } catch (NumberFormatException e) {
                System.err.println("[INPUT ERROR] Số tín chỉ phải là một số nguyên hợp lệ.");
            }
        }
    }

    /**
     * Reads a course title for update. Pressing Enter keeps the current value.
     */
    public static String getUpdateCourseTitle(Scanner scanner, String currentTitle) {
        while (true) {
            System.out.printf("Nhập tên khóa học mới (Ấn Enter để GIỮ '%s'): ", currentTitle);
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                return currentTitle;
            }
            if (Validator.isValidCourseTitle(input)) {
                return input.trim();
            }
        }
    }

    /**
     * Reads a course credit for update. Pressing Enter keeps the current value.
     */
    public static int getUpdateCourseCredit(Scanner scanner, int currentCredit) {
        while (true) {
            System.out.printf("Nhập số tín chỉ mới (Ấn Enter để GIỮ '%d'): ", currentCredit);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentCredit;
            }
            try {
                int credit = Integer.parseInt(input);
                if (Validator.isValidCourseCredit(credit)) {
                    return credit;
                }
            } catch (NumberFormatException e) {
                System.err.println("[INPUT ERROR] Số tín chỉ phải là một số nguyên hợp lệ.");
            }
        }
    }
}
