package utils;

import java.util.Scanner;
import java.util.function.Predicate;

public class InputUtil {

    public static String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readNonEmptyString(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            String input = readString(scanner, prompt);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(errorMsg);
        }
    }

    public static String readStringWithCondition(Scanner scanner, String prompt, Predicate<String> condition, String errorMsg) {
        while (true) {
            String input = readString(scanner, prompt);
            if (condition.test(input)) {
                return input;
            }
            System.out.println(errorMsg);
        }
    }

    public static String readStringOrKeep(Scanner scanner, String prompt, String currentValue) {
        System.out.print(prompt + " (leave empty to keep '" + currentValue + "'): ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? currentValue : input;
    }

    public static String readStringOrKeepWithCondition(Scanner scanner, String prompt, String currentValue, Predicate<String> condition, String errorMsg) {
        while (true) {
            System.out.print(prompt + " (leave empty to keep '" + currentValue + "'): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return currentValue;
            }
            if (condition.test(input)) {
                return input;
            }
            System.out.println(errorMsg);
        }
    }

    public static int readInt(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            String input = readString(scanner, prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(errorMsg);
            }
        }
    }

    public static int readIntInRange(Scanner scanner, String prompt, int min, int max, String errorMsg) {
        while (true) {
            int value = readInt(scanner, prompt, errorMsg);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println(errorMsg);
        }
    }

    public static double readDouble(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            String input = readString(scanner, prompt);
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println(errorMsg);
            }
        }
    }

    public static double readDoubleInRange(Scanner scanner, String prompt, double min, double max, String errorMsg) {
        while (true) {
            double value = readDouble(scanner, prompt, errorMsg);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println(errorMsg);
        }
    }
}
