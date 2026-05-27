package fa.training.view;

/**
 * Top-level application menu view.
 */
public class MainView {

    public void printMainMenu() {
        System.out.println("""

                ====== TRAINING CENTER MANAGEMENT ======
                1. Manage Students
                2. Manage Courses
                3. Manage Enrollment
                4. Query Practice
                5. Exit
                ========================================
                """);
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice. Please try again.");
    }

    public void printExitMessage() {
        System.out.println("Goodbye!");
    }
}
