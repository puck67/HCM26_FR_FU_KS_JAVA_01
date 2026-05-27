import dao.QuizDAO;
import model.Quiz;
import validation.Validator;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final QuizDAO dao = new QuizDAO();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      QUIZ MANAGEMENT SYSTEM          ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> addQuiz();
                case "2" -> displayAll();
                case "3" -> updateQuiz();
                case "4" -> deleteQuiz();
                case "5" -> searchById();
                case "6" -> { running = false; System.out.println("Goodbye!"); }
                default  -> System.out.println("[!] Invalid option.");
            }
        }
    }

    // ----------------------------------------------------------------
    private static void printMenu() {
        System.out.println("\n┌─────────────────────────────┐");
        System.out.println("│  1. Add new quiz            │");
        System.out.println("│  2. Display all quizzes     │");
        System.out.println("│  3. Update a quiz           │");
        System.out.println("│  4. Delete a quiz           │");
        System.out.println("│  5. Search quiz by ID       │");
        System.out.println("│  6. Exit                    │");
        System.out.println("└─────────────────────────────┘");
        System.out.print("Choice: ");
    }

    // ----------------------------------------------------------------
    private static void addQuiz() {
        System.out.println("\n--- Add New Quiz ---");
        try {
            String id = promptId(true);
            if (id == null) return;

            String question = promptNonEmpty("Question: ");
            if (question == null) return;

            String answer = promptNonEmpty("Answer: ");
            if (answer == null) return;

            int difficulty = promptDifficulty();
            if (difficulty == -1) return;

            String createdBy = promptNonEmpty("Created by: ");
            if (createdBy == null) return;

            Quiz q = new Quiz(id, question, answer, difficulty, createdBy);
            dao.add(q);
            System.out.println("Quiz added successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    private static void displayAll() {
        try {
            List<Quiz> list = dao.getAll();
            if (list.isEmpty()) {
                System.out.println("[!] No quizzes found.");
                return;
            }
            System.out.println("\n" + "─".repeat(120));
            System.out.printf("| %-8s | %-40s | %-30s | %-4s | %-15s |%n",
                "ID", "Question", "Answer", "Diff", "Created By");
            System.out.println("─".repeat(120));
            list.forEach(q -> System.out.println(q));
            System.out.println("─".repeat(120));
            System.out.println("Total: " + list.size() + " record(s).");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    private static void updateQuiz() {
        System.out.println("\n--- Update Quiz ---");
        try {
            String id = promptId(false);
            if (id == null) return;

            Quiz existing = dao.findById(id);
            if (existing == null) {
                System.out.println("[!] Quiz not found: " + id);
                return;
            }

            System.out.println("Current: " + existing);
            System.out.println("(Press Enter to keep current value)");

            String question = promptOrKeep("Question", existing.getQuestion());
            String answer   = promptOrKeep("Answer", existing.getAnswer());

            System.out.print("Difficulty (1-5) [" + existing.getDifficulty() + "]: ");
            String diffStr = sc.nextLine().trim();
            int difficulty = diffStr.isEmpty() ? existing.getDifficulty() : Validator.parseIntSafe(diffStr);
            if (!Validator.isValidDifficulty(difficulty)) {
                System.out.println("Invalid difficulty.");
                return;
            }

            String createdBy = promptOrKeep("Created by", existing.getCreatedBy());

            dao.update(new Quiz(id, question, answer, difficulty, createdBy));
            System.out.println("Quiz updated successfully.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    private static void deleteQuiz() {
        System.out.println("\n--- Delete Quiz ---");
        try {
            String id = promptId(false);
            if (id == null) return;

            Quiz existing = dao.findById(id);
            if (existing == null) {
                System.out.println("Quiz not found: " + id);
                return;
            }

            System.out.print("Confirm delete '" + id + "'? (y/n): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("Cancelled.");
                return;
            }

            dao.delete(id);
            System.out.println("Quiz deleted.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    private static void searchById() {
        System.out.println("\n--- Search Quiz by ID ---");
        try {
            String id = promptId(false);
            if (id == null) return;

            Quiz q = dao.findById(id);
            if (q == null) {
                System.out.println("No quiz found with ID: " + id);
            } else {
                System.out.println("\nFound:");
                System.out.println("  ID         : " + q.getId());
                System.out.println("  Question   : " + q.getQuestion());
                System.out.println("  Answer     : " + q.getAnswer());
                System.out.println("  Difficulty : " + q.getDifficulty() + "/5");
                System.out.println("  Created by : " + q.getCreatedBy());
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // Input helpers
    // ----------------------------------------------------------------
    private static String promptId(boolean checkUnique) throws SQLException {
        System.out.print("ID (2-10 alphanumeric): ");
        String id = sc.nextLine().trim();
        if (!Validator.isValidIdFormat(id)) {
            System.out.println("Invalid ID format.");
            return null;
        }
        if (checkUnique && dao.findById(id) != null) {
            System.out.println("ID already exists.");
            return null;
        }
        return id;
    }

    private static String promptNonEmpty(String label) {
        System.out.print(label);
        String val = sc.nextLine().trim();
        if (!Validator.isNotEmpty(val)) {
            System.out.println("Cannot be empty.");
            return null;
        }
        return val;
    }

    private static int promptDifficulty() {
        System.out.print("Difficulty (1-5): ");
        int d = Validator.parseIntSafe(sc.nextLine());
        if (!Validator.isValidDifficulty(d)) {
            System.out.println("Difficulty must be 1–5.");
            return -1;
        }
        return d;
    }

    private static String promptOrKeep(String label, String current) {
        System.out.print(label + " [" + current + "]: ");
        String val = sc.nextLine().trim();
        return val.isEmpty() ? current : val;
    }
}
