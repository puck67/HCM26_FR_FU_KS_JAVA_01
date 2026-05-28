package fa.training.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt(String message, int min, int max) {
        while (true) {
            try {
                System.out.print(message);
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) return value;
                System.out.printf("Vui lòng nhập số trong khoảng [%d - %d].\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Dữ liệu nhập vào phải là số nguyên hợp lệ.");
            }
        }
    }

    public static String readString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Lỗi: Nội dung không được để trống.");
        }
    }

    public static String readValidOptions(String message, String... options) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            for (String opt : options) {
                if (opt.equalsIgnoreCase(input)) return opt;
            }
            System.out.println("Lỗi: Giá trị nhập không hợp lệ. Vui lòng chọn đúng các tùy chọn đã cho.");
        }
    }

    public static LocalDate readDate(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Lỗi: Sai định dạng ngày. Vui lòng nhập theo cấu trúc: YYYY-MM-DD");
            }
        }
    }
}
