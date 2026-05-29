package fa.training.app;

import java.util.Map;
import java.util.Scanner;

public class MenuManager {

    private final Scanner scanner;

    public MenuManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public void runMenu(String title, Map<Integer, String> descriptions, Map<Integer, MenuAction> actions) {
        runMenu(title, descriptions, actions, true);
    }

    public void runMenu(String title, Map<Integer, String> descriptions, Map<Integer, MenuAction> actions,
                        boolean showContinuePrompt) {
        boolean running = true;
        while (running) {
            int width = title.length() + 6;
            for (String desc : descriptions.values()) {
                width = Math.max(width, desc.length() + 10);
            }
            width = Math.max(width, 40);

            String topBorder    = "╔" + "═".repeat(width) + "╗";
            String separator    = "╠" + "═".repeat(width) + "╣";
            String bottomBorder = "╚" + "═".repeat(width) + "╝";

            System.out.println("\n" + topBorder);
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

    public int readInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) {
                        System.out.print("⚠️ Input cannot be empty. Enter an integer: ");
                        continue;
                    }
                    return Integer.parseInt(input);
                } else {
                    return 0;
                }
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Invalid format. Enter an integer: ");
            } catch (Exception e) {
                System.out.print("⚠️ Error reading input. Enter an integer: ");
            }
        }
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            int val = readInt(prompt);
            if (val > 0) {
                return val;
            }
            System.out.println("⚠️ Value must be a positive integer greater than 0.");
        }
    }

    public int readNonNegativeInt(String prompt) {
        while (true) {
            int val = readInt(prompt);
            if (val >= 0) {
                return val;
            }
            System.out.println("⚠️ Value must be greater than or equal to 0.");
        }
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine().trim();
            }
        } catch (Exception e) {
            System.out.println("\n⚠️ [Error] Failed to read input.");
        }
        return "";
    }

    public String readNonEmptyString(String prompt) {
        while (true) {
            String val = readString(prompt);
            if (!val.isEmpty()) {
                return val;
            }
            System.out.println("⚠️ Input cannot be empty.");
        }
    }

    public String readStringWithLengthLimit(String prompt, int maxLength) {
        while (true) {
            String val = readString(prompt);
            if (val.length() <= maxLength) {
                return val;
            }
            System.out.printf("⚠️ Input length must not exceed %d characters (current length: %d).%n", maxLength, val.length());
        }
    }

    public String readStringWithChoices(String prompt, java.util.List<String> choices) {
        while (true) {
            String val = readString(prompt);
            for (String choice : choices) {
                if (choice.equalsIgnoreCase(val)) {
                    return choice; // return standard capitalized choice
                }
            }
            System.out.println("⚠️ Invalid choice. Must be one of: " + choices);
        }
    }

    public java.time.LocalDate readLocalDate(String prompt, boolean allowEmpty) {
        while (true) {
            String val = readString(prompt);
            if (allowEmpty && val.isEmpty()) {
                return java.time.LocalDate.now();
            }
            try {
                return java.time.LocalDate.parse(val, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("⚠️ Invalid date format. Please use yyyy-MM-dd (e.g., 2026-05-28).");
            }
        }
    }
}
