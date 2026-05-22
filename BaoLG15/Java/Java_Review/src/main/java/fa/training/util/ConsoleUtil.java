package fa.training.util;

import fa.training.entities.Movie;

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

    public static void printMovieTable(List<Movie> movies) {
        System.out.println();
        System.out.printf("| %-10s | %-30s | %-20s | %-15s | %-6s | %-6s |%n", "ID", "Title", "Director", "Genre",
                "Year", "Rating");
        System.out.println("|" + "-".repeat(12) + "|" + "-".repeat(32) + "|" + "-".repeat(22)
                + "|" + "-".repeat(17) + "|" + "-".repeat(8) + "|" + "-".repeat(8) + "|");
        movies.forEach(m -> System.out.println(m.toDisplayString()));
    }

    public static void close() {
        scanner.close();
    }
}
