package fa.training.util;

import fa.training.entities.Category;
import fa.training.entities.Manufacturer;
import fa.training.entities.Vehicle;

import java.util.List;
import java.util.Scanner;

public final class ConsoleUtil {

    private static final Scanner scanner = new Scanner(System.in);

    private ConsoleUtil() {
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt) {
        System.out.print(prompt);
        var line = scanner.nextLine().trim();
        return parseIntOrDefault(line, -1);
    }

    public static double readDouble(String prompt) {
        System.out.print(prompt);
        var line = scanner.nextLine().trim();
        return parseDoubleOrDefault(line, -1);
    }

    public static int parseIntOrDefault(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static double parseDoubleOrDefault(String s, double defaultValue) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static void printVehicleTable(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.println();
        System.out.printf("| %-10s | %-20s | %-12s | %-25s | %-15s | %-15s | %-12s |%n", 
                "ID", "Model", "Price ($)", "Owner Email", "Owner Phone", "Manufacturer", "Category");
        System.out.println("|" + "-".repeat(12) + "|" + "-".repeat(22) + "|" + "-".repeat(14)
                + "|" + "-".repeat(27) + "|" + "-".repeat(17) + "|" + "-".repeat(17) + "|" + "-".repeat(14) + "|");
        for (var v : vehicles) {
            System.out.printf("| %-10s | %-20s | %-12.2f | %-25s | %-15s | %-15s | %-12s |%n",
                    v.getId(),
                    v.getModel(),
                    v.getPrice(),
                    v.getOwnerEmail(),
                    v.getOwnerPhone(),
                    v.getManufacturer() != null ? v.getManufacturer().getName() : "N/A",
                    v.getCategory() != null ? v.getCategory().getName() : "N/A");
        }
        System.out.println();
    }

    public static void printManufacturerTable(List<Manufacturer> manufacturers) {
        if (manufacturers == null || manufacturers.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.println();
        System.out.printf("| %-10s | %-30s | %-20s |%n", "ID", "Name", "Country");
        System.out.println("|" + "-".repeat(12) + "|" + "-".repeat(32) + "|" + "-".repeat(22) + "|");
        for (var m : manufacturers) {
            System.out.printf("| %-10s | %-30s | %-20s |%n", m.getId(), m.getName(), m.getCountry());
        }
        System.out.println();
    }

    public static void printCategoryTable(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.println();
        System.out.printf("| %-10s | %-30s | %-50s |%n", "ID", "Name", "Description");
        System.out.println("|" + "-".repeat(12) + "|" + "-".repeat(32) + "|" + "-".repeat(52) + "|");
        for (var c : categories) {
            System.out.printf("| %-10s | %-30s | %-50s |%n", c.getId(), c.getName(), c.getDescription() != null ? c.getDescription() : "");
        }
        System.out.println();
    }
}
