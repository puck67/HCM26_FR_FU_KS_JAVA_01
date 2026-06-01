package fa.training;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Main entry point.
 *
 * Menu uses switch-case with lambda (Consumer<Scanner>) actions
 * to avoid code duplication and keep Service/DAO layers free of
 * Scanner / System.out — UI concerns stay in this class only.
 */
public class Main {

    private static final EmployeeDAO employeeDAO = new EmployeeDAO();

    public static void main(String[] args) {
        System.out.println("=== Hibernate Assignment 01 — H2 Database ===");

        // Seed one record so the list is never empty on first run
        seedSampleData();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = scanner.nextLine().trim();

                // Lambda actions keyed by menu choice ─────────────────────
                switch (choice) {
                    case "1":
                        handleListAll.accept(scanner);
                        break;
                    case "2":
                        handleFindById.accept(scanner);
                        break;
                    case "3":
                        handleInsert.accept(scanner);
                        break;
                    case "4":
                        handleUpdate.accept(scanner);
                        break;
                    case "5":
                        handleDelete.accept(scanner);
                        break;
                    case "0":
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }

    // ───────────────────────── Menu lambdas ────────────────────────────────

    private static final Consumer<Scanner> handleListAll = scanner -> {
        List<Employee> employees = employeeDAO.findAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            employees.forEach(System.out::println);
        }
    };

    private static final Consumer<Scanner> handleFindById = scanner -> {
        System.out.print("Enter employee ID: ");
        int id = readInt(scanner);
        Optional<Employee> result = employeeDAO.findEmployeeById(id);
        result.ifPresent(System.out::println);
        if (!result.isPresent()) {
            System.out.println("Employee with id=" + id + " not found.");
        }
    };

    private static final Consumer<Scanner> handleInsert = scanner -> {
        System.out.print("First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("First name and last name cannot be empty.");
            return;
        }

        Employee saved = employeeDAO.saveEmployee(new Employee(firstName, lastName));
        System.out.println("Saved: " + saved);
    };

    private static final Consumer<Scanner> handleUpdate = scanner -> {
        System.out.print("Enter employee ID to update: ");
        int id = readInt(scanner);
        System.out.print("New first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("New last name: ");
        String lastName = scanner.nextLine().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("First name and last name cannot be empty.");
            return;
        }

        boolean updated = employeeDAO.updateEmployeeById(id, firstName, lastName);
        System.out.println(updated ? "Updated successfully." : "Employee id=" + id + " not found.");
    };

    private static final Consumer<Scanner> handleDelete = scanner -> {
        System.out.print("Enter employee ID to delete: ");
        int id = readInt(scanner);
        boolean deleted = employeeDAO.deleteEmployeeById(id);
        System.out.println(deleted ? "Deleted successfully." : "Employee id=" + id + " not found.");
    };

    // ───────────────────────── Helpers ─────────────────────────────────────

    private static void printMenu() {
        System.out.println("\n--- EMPLOYEE MENU ---");
        System.out.println("1. List all employees");
        System.out.println("2. Find employee by ID");
        System.out.println("3. Add new employee");
        System.out.println("4. Update employee");
        System.out.println("5. Delete employee");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    /**
     * Reads an integer from console; prompts again on bad input.
     * Defensive programming: prevents NumberFormatException crashing the app.
     */
    private static int readInt(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.matches("-?\\d+")) {
                return Integer.parseInt(line);
            }
            System.out.print("Invalid input. Please enter a number: ");
        }
    }

    private static void seedSampleData() {
        List<Employee> existing = employeeDAO.findAllEmployees();
        if (existing.isEmpty()) {
            employeeDAO.saveEmployee(new Employee("Nguyen", "Van A"));
            employeeDAO.saveEmployee(new Employee("Tran", "Thi B"));
            System.out.println("Sample data seeded.");
        }
    }
}
