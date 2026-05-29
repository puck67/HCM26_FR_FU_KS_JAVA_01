package fa.training.main;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.util.HibernateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Build menu using Map<Integer, Runnable> + lambda
        Map<Integer, Runnable> menuActions = new HashMap<>();
        menuActions.put(1, Main::handleInsert);
        menuActions.put(2, Main::handleGetById);
        menuActions.put(3, Main::handleGetAll);
        menuActions.put(4, Main::handleUpdate);
        menuActions.put(5, Main::handleDelete);
        menuActions.put(6, () -> {
            System.out.println("Exiting program.");
            HibernateUtil.shutdown();
            System.exit(0);
        });

        while (true) {
            printMenu();
            System.out.print("Enter your choice: ");
            int choice = readInt();

            Runnable action = menuActions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Print menu using StringBuilder
    private static void printMenu() {
        String menu = new StringBuilder()
                .append("\n========== EMPLOYEE MANAGEMENT ==========\n")
                .append("1. Add Employee\n")
                .append("2. Find Employee by ID\n")
                .append("3. View all Employees\n")
                .append("4. Update Employee by ID\n")
                .append("5. Delete Employee by ID\n")
                .append("6. Exit\n")
                .append("==========================================")
                .toString();
        System.out.println(menu);
    }

    // Handle adding a new Employee
    private static void handleInsert() {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("First Name and Last Name must not be empty.");
            return;
        }

        employeeDAO.insertEmployee(new Employee(firstName, lastName));
    }

    // Handle finding Employee by ID
    private static void handleGetById() {
        System.out.print("Enter ID to search: ");
        int id = readInt();
        Employee employee = employeeDAO.getEmployeeById(id);
        if (employee != null) {
            System.out.println("Result: " + employee);
        }
    }

    // Handle displaying all Employees
    private static void handleGetAll() {
        List<Employee> list = employeeDAO.getAllEmployees();
        if (list == null || list.isEmpty()) {
            System.out.println("Employee list is empty.");
            return;
        }

        // Use StringBuilder to concatenate result string
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Employee List (").append(list.size()).append(" record(s)) ---\n");
        list.forEach(e -> sb.append(e).append("\n"));
        System.out.println(sb.toString());
    }

    // Handle updating Employee by ID
    private static void handleUpdate() {
        System.out.print("Enter ID to update: ");
        int id = readInt();
        System.out.print("Enter new First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter new Last Name: ");
        String lastName = scanner.nextLine().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("First Name and Last Name must not be empty.");
            return;
        }

        employeeDAO.updateEmployeeById(id, firstName, lastName);
    }

    // Handle deleting Employee by ID
    private static void handleDelete() {
        System.out.print("Enter ID to delete: ");
        int id = readInt();
        employeeDAO.deleteEmployeeById(id);
    }

    // Read integer from console and discard remaining newline
    private static int readInt() {
        int value = 0;
        try {
            value = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid integer.");
        }
        return value;
    }
}
