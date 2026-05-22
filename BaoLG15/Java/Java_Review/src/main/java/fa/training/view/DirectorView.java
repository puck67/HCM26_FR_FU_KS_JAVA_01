package fa.training.view;

import fa.training.entities.Director;
import java.util.List;

public class DirectorView {
    public void printMenu() {
        System.out.println("""
                
                --- Director Management ---
                1. Add new director
                2. Display all directors
                3. Update a director
                4. Delete a director
                5. Search director by name
                6. Back to main menu\
                """);
    }

    public void displayDirectors(List<Director> directors) {
        if (directors.isEmpty()) {
            System.out.println("\nNo directors found.");
            return;
        }
        System.out.printf("\n| %-5s | %-30s |%n", "ID", "Name");
        System.out.println("|-------|--------------------------------|");
        directors.forEach(d -> System.out.printf("| %-5d | %-30s |%n", d.getId(), d.getName()));
    }
    
    public void displayDirectorsSearch(List<Director> directors, String keyword) {
        if (directors.isEmpty()) {
            System.out.printf("  [!] No directors found matching '%s'.%n", keyword);
        } else {
            System.out.printf("Found %d director(s):%n", directors.size());
            displayDirectors(directors);
        }
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
