package fa.training.view;

import fa.training.entities.Genre;
import java.util.List;

public class GenreView {
    public void printMenu() {
        System.out.println("""
                
                --- Genre Management ---
                1. Add new genre
                2. Display all genres
                3. Update a genre
                4. Delete a genre
                5. Search genre by name
                6. Back to main menu\
                """);
    }

    public void displayGenres(List<Genre> genres) {
        if (genres.isEmpty()) {
            System.out.println("\nNo genres found.");
            return;
        }
        System.out.printf("\n| %-5s | %-30s |%n", "ID", "Name");
        System.out.println("|-------|--------------------------------|");
        genres.forEach(g -> System.out.printf("| %-5d | %-30s |%n", g.getId(), g.getName()));
    }

    public void displayGenresSearch(List<Genre> genres, String keyword) {
        if (genres.isEmpty()) {
            System.out.printf("  [!] No genres found matching '%s'.%n", keyword);
        } else {
            System.out.printf("Found %d genre(s):%n", genres.size());
            displayGenres(genres);
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
