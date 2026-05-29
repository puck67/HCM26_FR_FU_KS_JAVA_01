package org.example.util;

import java.util.Scanner;

public class InputUtil {

    public static int getInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer number.");
            }
        }
    }

    public static int getPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            int value = getInt(scanner, prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Value must be a positive integer (greater than 0).");
        }
    }

    public static String getString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public static Integer getOptionalInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be a positive integer.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer number or leave blank to keep current.");
            }
        }
    }
}
