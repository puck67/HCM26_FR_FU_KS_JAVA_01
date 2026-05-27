package fa.util;

import java.util.Scanner;

public class Validations {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String INTEGER_REGEX = "^-?[0-9]+$";
    private static final String POSITIVE_INTEGER_REGEX = "^[1-9][0-9]*$";

    public static int getMenuChoice(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches(INTEGER_REGEX)) {
                return Integer.parseInt(input);
            } else {
                System.out.println("Error: Choice must be an integer! Please try again.");
            }
        }
    }

    public static int getInteger(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches(INTEGER_REGEX)) {
                return Integer.parseInt(input);
            } else {
                System.out.println(errorMessage);
            }
        }
    }

    public static int getPositiveInteger(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches(POSITIVE_INTEGER_REGEX)) {
                return Integer.parseInt(input);
            } else {
                System.out.println(errorMessage);
            }
        }
    }

    public static String getString(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println(errorMessage);
            }
        }
    }

    public static String getStringWithRegex(String prompt, String regex, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches(regex)) {
                return input;
            } else {
                System.out.println(errorMessage);
            }
        }
    }
}
