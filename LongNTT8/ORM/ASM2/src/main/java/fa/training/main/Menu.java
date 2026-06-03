package fa.training.main;

import fa.training.util.InputUtil;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private String title;
    private List<MenuItem> items = new ArrayList<>();

    public Menu(String title) {
        this.title = title;
    }

    public void addItem(String label, Runnable action) {
        items.add(new MenuItem(label, action));
    }

    public void display() {
        while (true) {
            System.out.println("\n=== " + title + " ===");
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + ". " + items.get(i).getLabel());
            }
            System.out.println("0. Exit");

            int choice = InputUtil.getInt("Enter your choice: ");
            if (choice == 0) {
                break;
            } else if (choice > 0 && choice <= items.size()) {
                items.get(choice - 1).getAction().run();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static class MenuItem {
        private String label;
        private Runnable action;

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
