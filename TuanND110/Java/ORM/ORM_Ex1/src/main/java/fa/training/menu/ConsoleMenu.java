package fa.training.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleMenu {

    private final String title;
    private final Scanner scanner;
    private final List<MenuOption> options = new ArrayList<>();

    public ConsoleMenu(String title, Scanner scanner) {
        this.title = title;
        this.scanner = scanner;
    }

    public ConsoleMenu addOption(int key, String label, Runnable action) {
        options.add(new MenuOption(key, label, action));
        return this;
    }

    public void run() {
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            if (choice == 0) {
                System.out.println("Goodbye!");
                return;
            }

            Optional<MenuOption> selected = options.stream()
                    .filter(option -> option.getKey() == choice)
                    .findFirst();

            if (selected.isPresent()) {
                selected.get().getAction().run();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println(title);
        options.forEach(option -> System.out.println(option.getKey() + ". " + option.getLabel()));
        System.out.println("0. Exit");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
}

