package fa.training.view;

public class QueryView {
    public void printMenu() {
        System.out.println("\n--- Queries and Reports ---");
        System.out.println("1. Find students older than a given age (HQL)");
        System.out.println("2. Find students by name (Named Query)");
        System.out.println("3. List students and their courses (HQL Join)");
        System.out.println("4. Find courses with credit greater than a value (Criteria API)");
        System.out.println("5. Count number of students in each course (Aggregation Query)");
        System.out.println("6. Find students enrolled in a specific course");
        System.out.println("7. Back to main menu");
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice.");
    }
}
