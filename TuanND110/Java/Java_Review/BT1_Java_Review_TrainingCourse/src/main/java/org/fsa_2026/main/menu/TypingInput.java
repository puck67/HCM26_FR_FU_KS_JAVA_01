package org.fsa_2026.main.menu;

import org.fsa_2026.util.Validation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class TypingInput {
    // single shared scanner for the class
    private static final Scanner scan = new Scanner(System.in);

    // exposed fields kept for compatibility with existing code
    public static String input;
    public static int id;
    public static String getInput(String message, Scanner scan) {
        while (true) {
            System.out.print(message);
            input = scan.nextLine().trim();
            if (!Validation.isEmpty(input)) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public static int getNumberInput(String message, Scanner scan) {
        while (true) {
            String value = getInput(message, scan);
            if (Validation.isNumber(value)) {
                return Integer.parseInt(value);
            }
            System.out.println("Invalid number. Please enter a valid integer.");
        }
    }

    public static String getEmailInput(String message, Scanner scan) {
        while (true) {
            String value = getInput(message, scan);
            if (Validation.isEmail(value)) {
                return value;
            }
            System.out.println("Invalid email format. Please try again.");
        }
    }

    public static String getPhoneInput(String message, Scanner scan) {
        while (true) {
            String value = getInput(message, scan);
            if (Validation.isPhone(value)) {
                return value;
            }
            System.out.println("Invalid phone number. Must be exactly 10 digits.");
        }
    }

    public static Date getDateInput(String message, Scanner scan) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        while (true) {
            String value = getInput(message, scan);
            try {
                return format.parse(value);
            } catch (ParseException e) {
                System.out.println("Invalid date. Use format yyyy-MM-dd.");
            }
        }
    }
}
