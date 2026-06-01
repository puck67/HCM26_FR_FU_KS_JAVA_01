package fa.training.view;

public class EnrollmentView {
    public void printMenu() {
        System.out.println("\n--- Enrollment Management ---");
        System.out.println("1. Enroll a student in a course");
        System.out.println("2. Remove a student from a course");
        System.out.println("3. View courses of a student");
        System.out.println("4. View students of a course");
        System.out.println("5. Back to main menu");
    }
    
    public void printInvalidChoice() {
        System.out.println("Invalid choice.");
    }
}
