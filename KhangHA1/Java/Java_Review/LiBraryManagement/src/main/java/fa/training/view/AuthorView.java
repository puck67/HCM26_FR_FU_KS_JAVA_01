package fa.training.view;

import fa.training.model.Author;

import java.util.List;

public class AuthorView {

    public void printMenu() {

        System.out.println("""
                
                --- Author Management ---
                1. Add new author
                2. Display all authors
                3. Update author
                4. Delete author
                5. Back to main menu
                """);
    }

    public void displayAuthors(List<Author> authors) {

        if (authors.isEmpty()) {
            System.out.println("\nNo authors found.");
            return;
        }

        System.out.printf(
                "\n| %-5s | %-30s |%n",
                "ID",
                "Name");

        System.out.println(
                "|-------|--------------------------------|");

        authors.forEach(a ->
                System.out.printf(
                        "| %-5d | %-30s |%n",
                        a.getId(),
                        a.getName()));
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