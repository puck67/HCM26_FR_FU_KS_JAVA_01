package com.example.util;

import java.util.Scanner;

public class ConsoleUtil {

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            int val = readInt(scanner, prompt);
            if (ValidateUtil.isValidId(val)) {
                return val;
            }
            System.out.println("ID or numeric input must be a positive integer (greater than 0).");
        }
    }

    public static int readAge(Scanner scanner, String prompt) {
        while (true) {
            int age = readInt(scanner, prompt);
            if (ValidateUtil.isValidAge(age)) {
                return age;
            }
            System.out.println("Age must be between 5 and 100.");
        }
    }

    public static int readCredit(Scanner scanner, String prompt) {
        while (true) {
            int credit = readInt(scanner, prompt);
            if (ValidateUtil.isValidCredit(credit)) {
                return credit;
            }
            System.out.println("Credit must be between 1 and 10.");
        }
    }

    public static String readString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    public static String readName(Scanner scanner, String prompt) {
        while (true) {
            String name = readString(scanner, prompt);
            if (ValidateUtil.isValidName(name)) {
                return name.trim();
            }
            System.out.println("Invalid name. Name must contain only letters and spaces, and be between 2 and 50 characters.");
        }
    }

    public static String readCourseTitle(Scanner scanner, String prompt) {
        while (true) {
            String title = readString(scanner, prompt);
            if (ValidateUtil.isValidCourseTitle(title)) {
                return title.trim();
            }
            System.out.println("Invalid course title. Title length must be between 2 and 100 characters.");
        }
    }
}
