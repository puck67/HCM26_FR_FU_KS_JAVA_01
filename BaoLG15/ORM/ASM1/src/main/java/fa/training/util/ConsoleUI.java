package fa.training.util;

import fa.training.entities.Employee;

import java.util.List;

public class ConsoleUI {

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String DIM = "\u001B[2m";

    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String WHITE = "\u001B[97m";

    private static final String TL = "╔", TR = "╗", BL = "╚", BR = "╝";
    private static final String H = "═", V = "║";
    private static final String SL = "╟", SR = "╢", SH = "─";

    private static final int BOX_WIDTH = 62;

    private ConsoleUI() {
    }

    public static void printBanner() {
        System.out.println();
        line(TL, H, TR, CYAN);
        centeredLine(" Hibernate Assignment 01 ", BOLD + CYAN, CYAN);
        centeredLine(" fa.training  |  PostgreSQL  |  Java 21 ", DIM + WHITE, CYAN);
        line(BL, H, BR, CYAN);
        System.out.println();
    }

    public static void printSection(String title) {
        System.out.println();
        line(SL, SH, SR, BLUE);
        System.out.printf("%s%s  %s%s%s%n",
                V, BOLD + YELLOW, title, RESET, padding(title, BOX_WIDTH - 4) + BLUE + V + RESET);
        line(SL, SH, SR, BLUE);
    }

    public static void ok(String message) {
        System.out.printf("  %s✔%s  %s%s%n", GREEN, RESET, message, RESET);
    }

    public static void info(String message) {
        System.out.printf("  %sℹ%s  %s%s%n", CYAN, RESET, message, RESET);
    }

    public static void warn(String message) {
        System.out.printf("  %s⚠%s  %s%s%n", YELLOW, RESET, message, RESET);
    }

    public static void error(String message) {
        System.out.printf("  %s✖%s  %s%s%n", RED, RESET, message, RESET);
    }

    public static void detail(String label, Object value) {
        System.out.printf("  %s  %-20s%s %s%s%n",
                DIM + "│", label + RESET + CYAN, "→", WHITE + value, RESET);
    }

    public static void printEmployeeTable(List<Employee> employees) {
        if (employees.isEmpty()) {
            warn("No employees found.");
            return;
        }

        String hdr = BOLD + WHITE;

        System.out.println();
        System.out.printf("  %s┌──────┬──────────────────────┬──────────────────────┐%s%n", CYAN, RESET);
        System.out.printf("  %s│%s %-4s %s│%s %-20s %s│%s %-20s %s│%s%n",
                CYAN, hdr, "ID", RESET,
                CYAN, hdr + "First Name", RESET,
                CYAN, hdr + "Last Name", RESET,
                CYAN, RESET);
        System.out.printf("  %s├──────┼──────────────────────┼──────────────────────┤%s%n", CYAN, RESET);

        employees.stream().forEach(emp -> System.out.printf("  %s│%s %-4d %s│%s %-20s %s│%s %-20s %s│%s%n",
                CYAN, WHITE, emp.getId(), RESET,
                CYAN, WHITE + emp.getFirstName(), RESET,
                CYAN, WHITE + emp.getLastName(), RESET,
                CYAN, RESET));

        System.out.printf("  %s└──────┴──────────────────────┴──────────────────────┘%s%n", CYAN, RESET);
        System.out.printf("  %s  %d record(s) found%s%n%n", DIM, employees.size(), RESET);
    }

    public static void printFooter(boolean success) {
        System.out.println();
        line(TL, H, TR, success ? GREEN : RED);
        String msg = success ? "  All operations completed successfully  " : "  Completed with errors  ";
        centeredLine(msg, BOLD + (success ? GREEN : RED), success ? GREEN : RED);
        line(BL, H, BR, success ? GREEN : RED);
        System.out.println();
    }

    public static void printSqlHint(String sql) {
        System.out.println();
        System.out.printf("  %s┌─ SQL Hint %s%n", DIM + CYAN, RESET);
        System.out.printf("  %s│  %s%s%s%n", DIM + CYAN, MAGENTA, sql, RESET);
        System.out.printf("  %s└%s%n", DIM + CYAN, RESET);
    }

    public static void divider() {
        System.out.printf("  %s%s%s%n", DIM, "─".repeat(BOX_WIDTH - 2), RESET);
    }

    private static void line(String left, String fill, String right, String color) {
        System.out.printf("%s%s%s%s%s%s%n",
                color, left, fill.repeat(BOX_WIDTH), right, RESET, "");
    }

    private static void centeredLine(String text, String textColor, String borderColor) {
        int totalPad = BOX_WIDTH - text.length();
        int left = totalPad / 2;
        int right = totalPad - left;
        System.out.printf("%s%s%s%s%s%s%s%s%n",
                borderColor, V,
                " ".repeat(Math.max(0, left)),
                textColor, text, RESET,
                " ".repeat(Math.max(0, right)),
                borderColor + V + RESET);
    }

    private static String padding(String text, int totalWidth) {
        int len = text.length();
        int pad = totalWidth - len;
        return pad > 0 ? " ".repeat(pad) : "";
    }
}
