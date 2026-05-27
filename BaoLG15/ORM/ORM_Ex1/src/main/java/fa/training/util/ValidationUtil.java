package fa.training.util;

public class ValidationUtil {

    public static boolean validateStudentName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Validation Error: Name cannot be empty.");
            return false;
        }
        if (name.length() < 2 || name.length() > 50) {
            System.out.println("Validation Error: Name must be between 2 and 50 characters.");
            return false;
        }
        if (!name.matches("^[a-zA-Z\\s\\p{L}]+$")) {
            System.out.println("Validation Error: Name must contain only alphabetical characters and spaces.");
            return false;
        }
        return true;
    }

    public static boolean validateStudentAge(int age) {
        if (age < 5 || age > 100) {
            System.out.println("Validation Error: Age must be between 5 and 100.");
            return false;
        }
        return true;
    }

    public static boolean validateCourseTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Validation Error: Course title cannot be empty.");
            return false;
        }
        if (title.length() < 3 || title.length() > 100) {
            System.out.println("Validation Error: Course title must be between 3 and 100 characters.");
            return false;
        }
        return true;
    }

    public static boolean validateCourseCredit(int credit) {
        if (credit < 1 || credit > 10) {
            System.out.println("Validation Error: Course credit must be between 1 and 10.");
            return false;
        }
        return true;
    }
}
