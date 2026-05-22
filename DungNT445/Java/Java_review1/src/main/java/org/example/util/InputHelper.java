package org.example.util;

import java.util.Scanner;

public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Prompts the user for a non-empty string
     */
    public static String promptString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Error: Input cannot be empty. Please try again.");
        }
    }

    /**
     * Prompts the user for a string (can be empty, useful for updates)
     */
    public static String promptStringForUpdate(String message, String currentValue) {
        System.out.print(message + " [" + currentValue + "]: ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? currentValue : input;
    }

    /**
     * Prompts for a validated student code (format: SVxxx)
     */
    public static String promptStudentCode(String message) {
        while (true) {
            String input = promptString(message).toUpperCase();
            if (InputValidator.isValidStudentCode(input)) {
                return input;
            }
            System.out.println("Error: Student code must start with 'SV' followed by numbers (e.g. SV001).");
        }
    }

    /**
     * Prompts for a validated subject code (format: MHxxx)
     */
    public static String promptSubjectCode(String message) {
        while (true) {
            String input = promptString(message).toUpperCase();
            if (InputValidator.isValidSubjectCode(input)) {
                return input;
            }
            System.out.println("Error: Subject code must start with 'MH' followed by numbers (e.g. MH01).");
        }
    }

    /**
     * Prompts for a validated name (letters and spaces only)
     */
    public static String promptName(String message) {
        while (true) {
            String input = promptString(message);
            if (InputValidator.isValidName(input)) {
                return input;
            }
            System.out.println("Error: Name must only contain letters and spaces (no numbers or special characters).");
        }
    }

    public static String promptNameForUpdate(String message, String currentValue) {
        while (true) {
            System.out.print(message + " [" + currentValue + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentValue;
            }
            if (InputValidator.isValidName(input)) {
                return input;
            }
            System.out.println("Error: Name must only contain letters and spaces.");
        }
    }

    /**
     * Prompts for a validated email
     */
    public static String promptEmail(String message) {
        while (true) {
            String input = promptString(message);
            if (InputValidator.isValidEmail(input)) {
                return input;
            }
            System.out.println("Error: Invalid email format (e.g., example@domain.com).");
        }
    }

    public static String promptEmailForUpdate(String message, String currentValue) {
        while (true) {
            System.out.print(message + " [" + currentValue + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentValue;
            }
            if (InputValidator.isValidEmail(input)) {
                return input;
            }
            System.out.println("Error: Invalid email format.");
        }
    }

    /**
     * Prompts for a validated phone number
     */
    public static String promptPhone(String message) {
        while (true) {
            String input = promptString(message);
            if (InputValidator.isValidPhone(input)) {
                return input;
            }
            System.out.println("Error: Phone number must be 10 digits starting with 0 (e.g., 0912345678).");
        }
    }

    public static String promptPhoneForUpdate(String message, String currentValue) {
        while (true) {
            System.out.print(message + " [" + currentValue + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentValue;
            }
            if (InputValidator.isValidPhone(input)) {
                return input;
            }
            System.out.println("Error: Phone number must be 10 digits starting with 0.");
        }
    }

    /**
     * Prompts for an integer within range [min, max]
     */
    public static int promptInt(String message, int min, int max) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Error: Please enter an integer between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format. Please enter an integer.");
            }
        }
    }

    public static int promptIntForUpdate(String message, int currentValue, int min, int max) {
        while (true) {
            System.out.print(message + " [" + currentValue + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentValue;
            }
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Error: Please enter an integer between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format.");
            }
        }
    }

    /**
     * Prompts for a double within range [min, max]
     */
    public static double promptDouble(String message, double min, double max) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Error: Please enter a decimal number between %.1f and %.1f.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid decimal format. Please enter a double (e.g., 8.5).");
            }
        }
    }

    public static double promptDoubleForUpdate(String message, double currentValue, double min, double max) {
        while (true) {
            System.out.print(message + " [" + currentValue + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentValue;
            }
            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Error: Please enter a decimal number between %.1f and %.1f.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid decimal format.");
            }
        }
    }
}
