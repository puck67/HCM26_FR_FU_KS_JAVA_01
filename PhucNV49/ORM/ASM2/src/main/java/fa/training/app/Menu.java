package fa.training.app;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    // ANSI color codes for high-end console display
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    private final String title;
    private final List<MenuItem> items = new ArrayList<>();
    private final Scanner scanner;
    private boolean exit = false;

    public Menu(String title, Scanner scanner) {
        this.title = title;
        this.scanner = scanner;
    }

    public void addItem(String label, Runnable action) {
        items.add(new MenuItem(label, action));
    }

    public void displayAndRun() {
        exit = false;
        while (!exit) {
            printDivider();
            System.out.println(CYAN + BOLD + "   ╔════════════════════════════════════════════════════════╗" + RESET);
            System.out.printf(CYAN + BOLD + "   ║ %-54s ║%n" + RESET, centerText(title, 54));
            System.out.println(CYAN + BOLD + "   ╠════════════════════════════════════════════════════════╣" + RESET);
            for (int i = 0; i < items.size(); i++) {
                System.out.printf(CYAN + BOLD + "   ║ " + YELLOW + "%2d. " + WHITE + "%-50s" + CYAN + BOLD + " ║%n" + RESET, (i + 1), items.get(i).label);
            }
            System.out.println(CYAN + BOLD + "   ╚════════════════════════════════════════════════════════╝" + RESET);
            
            int choice = readInt("Select an option (1-" + items.size() + "): ", 1, items.size());
            printDivider();
            try {
                // Execute the lambda action
                items.get(choice - 1).action.run();
            } catch (Exception e) {
                printError("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    // Helper functions for safe, non-crashing inputs
    public int readInt(String prompt) {
        while (true) {
            System.out.print(BLUE + prompt + RESET);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printWarning("Invalid input! Please enter a valid integer.");
            }
        }
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            int val = readInt(prompt);
            if (val >= min && val <= max) {
                return val;
            }
            printWarning("Choice out of range! Must be between " + min + " and " + max + ".");
        }
    }

    public String readString(String prompt) {
        while (true) {
            System.out.print(BLUE + prompt + RESET);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            printWarning("Input cannot be empty.");
        }
    }

    public String readString(String prompt, List<String> allowedOptions) {
        while (true) {
            String input = readString(prompt);
            for (String option : allowedOptions) {
                if (option.equalsIgnoreCase(input)) {
                    return option; // returns matching case-exact option
                }
            }
            printWarning("Invalid option! Allowed values are: " + allowedOptions);
        }
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(BLUE + prompt + RESET);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                printWarning("Invalid date format! Please use YYYY-MM-DD (e.g. 2026-05-28).");
            }
        }
    }

    public static void printSuccess(String message) {
        System.out.println(GREEN + "✓ " + message + RESET);
    }

    public static void printWarning(String message) {
        System.out.println(YELLOW + "⚠ " + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + "✗ " + message + RESET);
    }

    public static void printDivider() {
        System.out.println();
    }

    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        while (sb.length() < width) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private static class MenuItem {
        String label;
        Runnable action;

        MenuItem(String label, Runnable action) {
            this.label = label;
            this.action = action;
        }
    }
}
