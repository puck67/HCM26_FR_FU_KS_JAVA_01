package fa.training;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println(" Hibernate Assignment 01 - Interactive Menu");
        System.out.println("==============================================");

        try {
            // Initialize Hibernate
            System.out.println("Initializing Database Connection...");
            HibernateUtil.getSessionFactory(); // trigger init
            System.out.println("Connected to Database successfully.");

            boolean running = true;
            while (running) {
                printMenu();
                int choice = getIntInput("Enter your choice (1-6): ");

                switch (choice) {
                    case 1:
                        addEmployee();
                        break;
                    case 2:
                        showAllEmployees();
                        break;
                    case 3:
                        findEmployeeById();
                        break;
                    case 4:
                        updateEmployee();
                        break;
                    case 5:
                        deleteEmployee();
                        break;
                    case 6:
                        running = false;
                        System.out.println("Exiting application...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please select from 1 to 6.");
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal Error: " + e.getMessage());
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
            System.out.println("Database connection closed.");
        }
    }

    private static void printMenu() {
        System.out.println("\n--- EMPLOYEE MANAGEMENT MENU ---");
        System.out.println("1. Add a new Employee");
        System.out.println("2. Display all Employees");
        System.out.println("3. Find Employee by ID");
        System.out.println("4. Update an Employee");
        System.out.println("5. Delete an Employee");
        System.out.println("6. Exit");
    }

    private static void addEmployee() {
        System.out.println("\n[ Add New Employee ]");
        while (true) {
            String firstName = getStringInput("Enter First Name (letters only, max 50 chars): ");
            String lastName = getStringInput("Enter Last Name (letters only, max 50 chars): ");

            if (employeeDAO.existsByFullName(firstName, lastName)) {
                System.out.println("=> WARNING: An employee with the name '" + firstName + " " + lastName + "' already exists in the database!");
                System.out.println("=> Please enter a different name.");
                continue; // Loop back to ask again
            }

            Employee emp = new Employee(firstName, lastName);
            if (employeeDAO.insertEmployee(emp)) {
                System.out.println("=> Success! Inserted: " + emp);
            } else {
                System.out.println("=> Failed to insert employee.");
            }
            break;
        }
    }

    private static void showAllEmployees() {
        System.out.println("\n[ All Employees ]");
        List<Employee> employees = employeeDAO.getAllEmployees();
        if (employees == null || employees.isEmpty()) {
            System.out.println("=> No employees found.");
            return;
        }
        employees.forEach(emp -> System.out.println("  -> " + emp));
    }

    private static void findEmployeeById() {
        System.out.println("\n[ Find Employee ]");
        int id = getIntInput("Enter Employee ID to find: ");
        Employee found = employeeDAO.getEmployeeByID(id);
        if (found != null) {
            System.out.println("=> Found: " + found);
        } else {
            System.out.println("=> Employee with ID=" + id + " not found.");
        }
    }

    private static void updateEmployee() {
        System.out.println("\n[ Update Employee ]");
        int id = getIntInput("Enter Employee ID to update: ");
        Employee found = employeeDAO.getEmployeeByID(id);
        
        if (found == null) {
            System.out.println("=> Employee with ID=" + id + " not found.");
            return;
        }
        
        System.out.println("=> Current Data: " + found);
        while (true) {
            String firstName = getStringInput("Enter new First Name (letters only, max 50 chars): ");
            String lastName = getStringInput("Enter new Last Name (letters only, max 50 chars): ");

            // Check if they are actually changing it to something that already exists
            if (!found.getFirstName().equals(firstName) || !found.getLastName().equals(lastName)) {
                if (employeeDAO.existsByFullName(firstName, lastName)) {
                    System.out.println("=> WARNING: Another employee with the name '" + firstName + " " + lastName + "' already exists!");
                    System.out.println("=> Please enter a different name.");
                    continue;
                }
            }

            if (employeeDAO.updateEmployeeByID(id, firstName, lastName)) {
                System.out.println("=> Success! Updated employee ID=" + id);
            } else {
                System.out.println("=> Failed to update employee.");
            }
            break;
        }
    }

    private static void deleteEmployee() {
        System.out.println("\n[ Delete Employee ]");
        int id = getIntInput("Enter Employee ID to delete: ");
        if (employeeDAO.deleteEmployeeById(id)) {
            System.out.println("=> Success! Deleted employee ID=" + id);
        } else {
            System.out.println("=> Employee with ID=" + id + " not found to delete.");
        }
    }

    // --- Validation Methods ---

    /**
     * Prompts the user until a valid integer is entered.
     */
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid number.");
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid format. Please enter a valid integer number.");
            }
        }
    }

    /**
     * Prompts the user until a valid string is entered (letters and spaces only, 1-50 chars).
     */
    private static String getStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid name.");
                continue;
            }
            if (input.length() > 50) {
                System.out.println("Input too long. Maximum allowed is 50 characters.");
                continue;
            }
            // Regex to match only letters, spaces, hyphens and apostrophes (supports Unicode)
            if (!input.matches("^[\\p{L} .'-]+$")) {
                System.out.println("Invalid characters. Please use letters only.");
                continue;
            }
            return input;
        }
    }
}
