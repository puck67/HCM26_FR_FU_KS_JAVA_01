package fa.training.util;

import java.util.Scanner;

/**
 * Utility class to read and validate user input from Console.
 */
public class Validator {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads an integer within a specified range from console.
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("⚠️ Lỗi: Giá trị phải nằm trong khoảng [%d - %d]. Vui lòng nhập lại!\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Lỗi: Định dạng không hợp lệ, vui lòng nhập một số nguyên!");
            }
        }
    }

    /**
     * Reads a non-empty string or standard string from console.
     */
    public static String readString(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!allowEmpty && input.isEmpty()) {
                System.out.println("⚠️ Lỗi: Nội dung không được để trống!");
                continue;
            }
            return input;
        }
    }
}
