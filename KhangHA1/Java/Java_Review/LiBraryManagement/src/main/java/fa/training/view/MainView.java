package fa.training.view;

public class MainView {

    public void printMainMenu() {

        System.out.println("""
                
                ========= LIBRARY MANAGEMENT =========
                1. Manage Authors
                2. Manage Categories
                3. Manage Books
                4. Exit
                ======================================
                """);
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice.");
    }

    public void printExitMessage() {
        System.out.println("Goodbye!");
    }
}