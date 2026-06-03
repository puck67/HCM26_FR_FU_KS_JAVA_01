package fa.training.view;

public class StudentView {
    public void printMenu() {
        System.out.println("\n--- Student Management ---");
        System.out.println("1. Create a new student");
        System.out.println("2. Update student information");
        System.out.println("3. Delete a student");
        System.out.println("4. View student by ID");
        System.out.println("5. List all students");
        System.out.println("6. Back to main menu");
    }
    
    public void printInvalidChoice() {
        System.out.println("Invalid choice.");
    }
}
