package fa.training.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final String title;
    private final List<MenuItem> items = new ArrayList<>();
    private boolean isExit = false;

    public Menu(String title) {
        this.title = title;
    }

    public void addItem(String label, Runnable action) {
        items.add(new MenuItem(label, action));
    }

    public void show(Scanner scanner) {
        isExit = false;
        boolean shouldPrintMenu = true;
        while (!isExit) {
            if (shouldPrintMenu) {
                System.out.println("\n=======================================================");
                System.out.println("        " + title.toUpperCase());
                System.out.println("=======================================================");
                for (int i = 0; i < items.size(); i++) {
                    System.out.println((i + 1) + ". " + items.get(i).getLabel());
                }
            }
            System.out.print("Please enter your choice (1-" + items.size() + "): ");
            shouldPrintMenu = true;

            if (!scanner.hasNextLine()) {
                break;
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                shouldPrintMenu = false;
                continue;
            }
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= items.size()) {
                    items.get(choice - 1).getAction().run();
                } else {
                    System.out.println("Invalid choice! Please select between 1 and " + items.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer choice!");
            }
        }
    }

    public void exitMenu() {
        this.isExit = true;
    }

    private static class MenuItem {
        private final String label;
        private final Runnable action;

        public MenuItem(String label, Runnable action) {
            this.label = label;
            this.action = action;
        }

        public String getLabel() {
            return label;
        }

        public Runnable getAction() {
            return action;
        }
    }
}
