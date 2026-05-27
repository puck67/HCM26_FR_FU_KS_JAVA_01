package utils;

public class ConsoleUtils {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    public static void printError(String message) {
        System.out.println(RED + BOLD + " [ERROR] " + RESET + RED + message + RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(GREEN + BOLD + " [SUCCESS] " + RESET + GREEN + message + RESET);
    }

    public static void printHeader(String title) {
        String border = "=".repeat(title.length() + 10);
        System.out.println("\n" + BLUE + BOLD + border + RESET);
        System.out.println(BLUE + BOLD + "     " + title.toUpperCase() + RESET);
        System.out.println(BLUE + BOLD + border + RESET);
    }
    
    public static void printMenuOption(String key, String description) {
        System.out.println(BLUE + BOLD + "  [" + key + "] " + RESET + description);
    }
}
