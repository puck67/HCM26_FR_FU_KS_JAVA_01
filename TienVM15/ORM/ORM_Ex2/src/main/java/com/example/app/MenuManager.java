package com.example.app;

import java.util.Map;
import java.util.Scanner;

/**
 * MenuManager encapsulates rendering and dispatching of interactive CLI menus
 * using lambda-based MenuAction entries.
 */
public class MenuManager {

    private final Scanner scanner;

    public MenuManager(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Renders a menu — shows "Press ENTER to continue..." after each action.
     * Suitable for submenus where quick CRUD actions need a pause.
     *
     * @param title        Menu title displayed in the header
     * @param descriptions Ordered map of choice -> description label
     * @param actions      Ordered map of choice -> MenuAction lambda
     */
    public void runMenu(String title, Map<Integer, String> descriptions, Map<Integer, MenuAction> actions) {
        runMenu(title, descriptions, actions, true);
    }

    /**
     * Renders a menu with the given title and loops until the user enters 0 (Back/Exit).
     *
     * @param title               Menu title displayed in the header
     * @param descriptions        Ordered map of choice -> description label
     * @param actions             Ordered map of choice -> MenuAction lambda
     * @param showContinuePrompt  If false, skip "Press ENTER to continue..." after each action.
     *                            Set false for top-level menus whose actions launch submenus.
     */
    public void runMenu(String title, Map<Integer, String> descriptions, Map<Integer, MenuAction> actions,
                        boolean showContinuePrompt) {
        boolean running = true;
        while (running) {
            // Find max width needed
            int width = title.length() + 6;
            for (String desc : descriptions.values()) {
                width = Math.max(width, desc.length() + 10);
            }
            width = Math.max(width, 40); // default minimum width

            // Generate borders
            String topBorder    = "╔" + "═".repeat(width) + "╗";
            String separator    = "╠" + "═".repeat(width) + "╣";
            String bottomBorder = "╚" + "═".repeat(width) + "╝";

            System.out.println("\n" + topBorder);
            // Center title
            int titlePadding = (width - title.length()) / 2;
            String titleFormat = "║" + " ".repeat(titlePadding) + "%s" + " ".repeat(width - title.length() - titlePadding) + "║";
            System.out.printf(titleFormat + "%n", title);
            System.out.println(separator);

            for (Map.Entry<Integer, String> entry : descriptions.entrySet()) {
                String optionStr = String.format("  [%d] %s", entry.getKey(), entry.getValue());
                int rightPadding = width - optionStr.length();
                if (rightPadding < 0) rightPadding = 0;
                System.out.printf("║%s" + " ".repeat(rightPadding) + "║%n", optionStr);
            }
            System.out.println(bottomBorder);

            int choice = readInt("👉 Enter your choice: ");
            if (choice == 0) {
                // Execute exit/back action if defined (e.g. shutdown + System.exit for main menu)
                MenuAction exitAction = actions.get(0);
                if (exitAction != null) exitAction.execute();
                running = false;
            } else {
                MenuAction action = actions.get(choice);
                if (action != null) {
                    action.execute();
                } else {
                    System.out.println("❌ Invalid selection. Try again.");
                }
                if (running && showContinuePrompt) {
                    readString("⌨️ Press ENTER to continue...");
                }
            }
        }
    }

    /** Read a validated integer from stdin */
    public int readInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) {
                        System.out.print("Input cannot be empty. Enter an integer: ");
                        continue;
                    }
                    return Integer.parseInt(input);
                } else {
                    return 0;
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid format. Enter an integer: ");
            } catch (Exception e) {
                System.out.print("Error reading input. Enter an integer: ");
            }
        }
    }

    /** Read a line of string from stdin */
    public String readString(String prompt) {
        System.out.print(prompt);
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine().trim();
            }
        } catch (Exception e) {
            System.out.println("\n[Error] Failed to read input.");
        }
        return "";
    }
}
