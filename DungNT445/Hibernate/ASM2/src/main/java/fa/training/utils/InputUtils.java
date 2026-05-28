package fa.training.utils;

import java.time.LocalDate;
import java.util.Scanner;

public class InputUtils {

    /**
     * Reads a valid integer from the console.
     */
    public static int readIntInput(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (Validator.isValidInteger(input)) {
                return Integer.parseInt(input);
            }
            System.out.println("Lỗi: Vui lòng nhập vào một số nguyên hợp lệ.");
        }
    }

    /**
     * Reads a valid positive integer from the console (greater than 0).
     */
    public static int readPositiveIntInput(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (Validator.isValidPositiveInteger(input)) {
                return Integer.parseInt(input);
            }
            System.out.println("Lỗi: Giá trị nhập vào phải là số nguyên dương lớn hơn 0.");
        }
    }

    /**
     * Reads a non-empty string from the console.
     */
    public static String readStringInput(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (Validator.isValidString(input)) {
                return input;
            }
            System.out.println("Lỗi: Không được để trống dữ liệu nhập vào.");
        }
    }

    /**
     * Reads a valid date (yyyy-MM-dd) from the console.
     */
    public static LocalDate readDateInput(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (Validator.isValidDate(input)) {
                return LocalDate.parse(input);
            }
            System.out.println("Lỗi: Định dạng ngày không đúng. Vui lòng nhập theo cấu trúc yyyy-MM-dd (Ví dụ: 2026-05-28).");
        }
    }

    /**
     * Reads and validates seat status ('Available', 'Not Available', 'Booked').
     */
    public static String readValidatedStatusInput(Scanner scanner) {
        while (true) {
            String status = readStringInput(scanner, "Nhập trạng thái ghế ('Available', 'Not Available', 'Booked'): ");
            if (Validator.isValidSeatStatus(status)) {
                return status;
            }
            System.out.println("Lỗi: Trạng thái không hợp lệ! Chỉ được nhập một trong ba giá trị quy định.");
        }
    }

    /**
     * Reads and validates seat type ('VIP', 'Normal').
     */
    public static String readValidatedTypeInput(Scanner scanner) {
        while (true) {
            String type = readStringInput(scanner, "Nhập loại ghế ('VIP', 'Normal'): ");
            if (Validator.isValidSeatType(type)) {
                return type;
            }
            System.out.println("Lỗi: Loại ghế không hợp lệ! Chỉ được nhập 'VIP' hoặc 'Normal'.");
        }
    }
}
