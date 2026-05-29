package fa.training.view;

public class CourseView {
    public void printMenu() {
        System.out.println("\n--- Course Management ---");
        System.out.println("1. Create a new course");
        System.out.println("2. Update course information");
        System.out.println("3. Delete a course");
        System.out.println("4. View course by ID");
        System.out.println("5. List all courses");
        System.out.println("6. Back to main menu");
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice.");
    }
}
