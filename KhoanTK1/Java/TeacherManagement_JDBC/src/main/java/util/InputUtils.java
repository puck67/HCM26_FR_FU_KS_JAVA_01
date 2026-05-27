package util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class InputUtils {

    private static final Scanner SCANNER = new Scanner(System.in);

    // Validate email format
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Validate phone (starts with 0, at least 10 digits)
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^0\\d{9,}$");

    private InputUtils() {}

    public static String getString(String prompt) {
        String value;
        do {
            System.out.print(prompt);
            value = SCANNER.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("  [!] This field cannot be empty. Please try again.");
            }
        } while (value.isEmpty());
        return value;
    }

    public static String getEmail(String prompt) {
        String email;
        do {
            System.out.print(prompt);
            email = SCANNER.nextLine().trim();
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                System.out.println("  [!] Invalid email format. Example: user@example.com");
            }
        } while (!EMAIL_PATTERN.matcher(email).matches());
        return email;
    }

    public static String getPhone(String prompt) {
        String phone;
        do {
            System.out.print(prompt);
            phone = SCANNER.nextLine().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                System.out.println("  [!] Phone must start with '0' and contain at least 10 digits.");
            }
        } while (!PHONE_PATTERN.matcher(phone).matches());
        return phone;
    }

    public static double getPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    System.out.println("  [!] Value must be greater than 0.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Please enter a numeric value (e.g. 3500.0).");
            }
        }
    }

    public static String getUpdateString(String prompt, String oldValue) {
        System.out.print(prompt + " [current: " + oldValue + "] > ");
        String input = SCANNER.nextLine().trim();
        return input.isEmpty() ? oldValue : input;
    }

    // Custom method for updating phone
    public static String getUpdatePhone(String prompt, String oldValue) {
        while (true) {
            System.out.print(prompt + " [current: " + oldValue + "] > ");
            String input = SCANNER.nextLine().trim();
            if (input.isEmpty()) {
                return oldValue; // Keep old value if empty
            }
            if (PHONE_PATTERN.matcher(input).matches()) {
                return input;    // Valid new phone
            } else {
                System.out.println("  [!] Phone must start with '0' and contain at least 10 digits.");
            }
        }
    }

    public static double getUpdateDouble(String prompt, double oldValue) {
        while (true) {
            System.out.printf("%s [current: %.2f] > ", prompt, oldValue);
            String input = SCANNER.nextLine().trim();
            if (input.isEmpty()) {
                return oldValue;
            }
            try {
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    System.out.println("  [!] Value must be greater than 0.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Please enter a numeric value (e.g. 3500.0).");
            }
        }
    }

    public static String getRawLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    // Custom method for updating email
    public static String getUpdateEmail(String prompt, String oldValue) {
        while (true) {
            System.out.print(prompt + " [current: " + oldValue + "] > ");
            String input = SCANNER.nextLine().trim();
            if (input.isEmpty()) {
                return oldValue; // Keep old value if empty
            }
            if (EMAIL_PATTERN.matcher(input).matches()) {
                return input;    // Valid new email
            } else {
                System.out.println("  [!] Invalid email format. Example: user@example.com");
            }
        }
    }
}