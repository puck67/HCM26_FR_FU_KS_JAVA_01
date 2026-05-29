package fa.training.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtils {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Loi: Phai nhap vao mot so nguyen hop le!");
            }
        }
    }

    public static int readPositiveInt(String prompt) {
        while (true) {
            int val = readInt(prompt);
            if (val <= 0) {
                System.out.println("Loi: So nhap vao phai lon hon 0!");
                continue;
            }
            return val;
        }
    }

    public static String readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            String input = scanner.nextLine().trim();
            try {
                LocalDate.parse(input, DATE_FORMATTER);
                return input;
            } catch (DateTimeParseException e) {
                System.out.println("Loi: Ngay khong dung dinh dang yyyy-MM-dd! Vui long nhap lai.");
            }
        }
    }

    public static String readSeatStatus(String prompt) {
        while (true) {
            System.out.print(prompt + " (Available | Not Available | Booked): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Available")) {
                return "Available";
            } else if (input.equalsIgnoreCase("Not Available") || input.equalsIgnoreCase("NotAvailable")) {
                return "Not Available";
            } else if (input.equalsIgnoreCase("Booked")) {
                return "Booked";
            } else {
                System.out.println("Loi: Trang thai ghe khong hop le! Chi chap nhan 'Available', 'Not Available', hoặc 'Booked'.");
            }
        }
    }

    public static String readSeatType(String prompt) {
        while (true) {
            System.out.print(prompt + " (VIP | Normal): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("VIP")) {
                return "VIP";
            } else if (input.equalsIgnoreCase("Normal")) {
                return "Normal";
            } else {
                System.out.println("Loi: Loai ghe khong hop le! Chi chap nhan 'VIP' hoac 'Normal'.");
            }
        }
    }
}
