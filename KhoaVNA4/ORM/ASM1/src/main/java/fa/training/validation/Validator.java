package fa.training.validation;

import java.util.Scanner;

public class Validator {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static String getValidName(String fieldName) {
        while (true) {
            System.out.print("Enter " + fieldName + ": ");
            String input = SCANNER.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(fieldName + " cannot be empty.");
                continue;
            }
            if (input.length() > 50) {
                System.out.println(fieldName + " cannot exceed 50 characters.");
                continue;
            }
            if (!input.matches("^[a-zA-Z\\sÀ-ỹ]+$")) {
                System.out.println(fieldName + " can only contain letters and spaces.");
                continue;
            }
            return input;
        }
    }

    public static int getValidInt(String label) {
        while (true) {
            System.out.print(label);
            String input = SCANNER.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("Value must be a positive integer.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter digits only.");
            }
        }
    }
}
