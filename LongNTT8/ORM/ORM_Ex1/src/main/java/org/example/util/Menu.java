package org.example.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private final String title;
    private final Map<String, Runnable> actions = new LinkedHashMap<>();
    private final Map<String, String> descriptions = new LinkedHashMap<>();

    public Menu(String title) {
        this.title = title;
    }

    public void addOption(String key, String description, Runnable action) {
        descriptions.put(key, description);
        actions.put(key, action);
    }

    public void run(Scanner scanner) {
        while (true) {
            System.out.println("\n=== " + title + " ===");
            descriptions.forEach((k, v) -> System.out.println(k + ". " + v));
            System.out.println("0. Back/Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            if ("0".equals(choice)) {
                break;
            }

            Runnable action = actions.get(choice);
            if (action != null) {
                try {
                    action.run();
                } catch (Exception e) {
                    System.out.println("Error executing action: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid option!");
            }
        }
    }
}
