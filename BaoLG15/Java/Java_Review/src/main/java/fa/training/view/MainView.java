package fa.training.view;

public class MainView {
    public void printMainMenu() {
        System.out.println("""
                
                ===== MOVIE MANAGEMENT SYSTEM =====
                1. Add new movie
                2. Display all movies
                3. Update a movie
                4. Delete a movie
                5. Search movie by ID
                6. Search movies by title
                7. Manage Directors
                8. Manage Genres
                9. Exit
                ====================================\
                """);
    }

    public void printGoodbye() {
        System.out.println("Goodbye!");
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice. Try again.");
    }
}
