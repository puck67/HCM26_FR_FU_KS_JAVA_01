package fa.training.view;

public class MainView {
    public void printMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Queries and Reports");
        System.out.println("5. Exit");
    }

    public void printExitMessage() {
        System.out.println("Exiting the application. Goodbye!");
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice. Please try again.");
    }
}
