package fa.training.view;

import fa.training.model.Category;

import java.util.List;

public class CategoryView {

    public void printMenu() {

        System.out.println("""
                
                --- Category Management ---
                1. Add new category
                2. Display all categories
                3. Update category
                4. Delete category
                5. Back to main menu
                """);
    }

    public void displayCategories(
            List<Category> categories) {

        if (categories.isEmpty()) {
            System.out.println(
                    "\nNo categories found.");
            return;
        }

        System.out.printf(
                "\n| %-5s | %-30s |%n",
                "ID",
                "Name");

        System.out.println(
                "|-------|--------------------------------|");

        categories.forEach(c ->
                System.out.printf(
                        "| %-5d | %-30s |%n",
                        c.getId(),
                        c.getName()));
    }

    public void printHeader(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    public void printSuccess(String message) {
        System.out.println("  [+] " + message);
    }

    public void printError(String message) {
        System.out.println("  [x] " + message);
    }

    public void printWarning(String message) {
        System.out.println("  [!] " + message);
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice.");
    }
}