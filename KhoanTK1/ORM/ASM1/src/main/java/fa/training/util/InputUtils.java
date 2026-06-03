package fa.training.util;

import java.util.Scanner;

public class InputUtils {

    private static final Scanner scanner = new Scanner(System.in);

    // Read non-empty string
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input == null || input.trim().isEmpty()) {
                System.out.println("Loi: Thong tin nhap khong duoc de trong!");
                continue;
            }
            return input.trim();
        }
    }

    // Read positive integer
    public static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input == null || input.trim().isEmpty()) {
                System.out.println("Loi: Khong duoc de trong!");
                continue;
            }
            try {
                int value = Integer.parseInt(input.trim());
                if (value <= 0) {
                    System.out.println("Loi: Phai nhap so lon hon 0!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Loi: Phai nhap vao mot so nguyen hop le!");
            }
        }
    }

    // Read any integer (like menu choices)
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Loi: Phai nhap vao mot so nguyen!");
            }
        }
    }
}
