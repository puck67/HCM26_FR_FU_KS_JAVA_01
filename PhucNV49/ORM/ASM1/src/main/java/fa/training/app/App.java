package fa.training.app;

import fa.training.dao.EmployeeDAO;
import fa.training.dao.impl.EmployeeDAOImpl;
import fa.training.entities.Employee;
import fa.training.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class App {
    private static final EmployeeDAO employeeDAO = new EmployeeDAOImpl();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu mainMenu = new Menu("Employee Directory (H2 DB)", scanner);

        mainMenu.addItem("Create Employee", () -> createEmployee(mainMenu));
        mainMenu.addItem("View Employee by ID", () -> viewEmployee(mainMenu));
        mainMenu.addItem("List All Employees", () -> listEmployees());
        mainMenu.addItem("Update Employee", () -> updateEmployee(mainMenu));
        mainMenu.addItem("Delete Employee", () -> deleteEmployee(mainMenu));
        mainMenu.addItem("Run Automated Tests (Happy & Unhappy Cases)", () -> runAutomatedTests());
        mainMenu.addItem("Exit", () -> {
            Menu.printSuccess("Shutting down Hibernate SessionFactory...");
            try {
                HibernateUtil.shutdown();
            } catch (Exception e) {
                Menu.printError("Error closing SessionFactory: " + e.getMessage());
            }
            Menu.printSuccess("Goodbye!");
            mainMenu.setExit(true);
        });

        mainMenu.displayAndRun();
        scanner.close();
    }

    private static void createEmployee(Menu menu) {
        System.out.println(Menu.CYAN + "=== Create New Employee ===" + Menu.RESET);
        String firstName = menu.readString("Enter First Name: ");
        String lastName = menu.readString("Enter Last Name: ");

        Employee emp = new Employee(firstName, lastName);
        boolean success = employeeDAO.insertEmployee(emp);
        if (success) {
            Menu.printSuccess("Employee inserted successfully with generated ID: " + emp.getId());
        } else {
            Menu.printError("Failed to insert employee.");
        }
    }

    private static void viewEmployee(Menu menu) {
        System.out.println(Menu.CYAN + "=== View Employee by ID ===" + Menu.RESET);
        int id = menu.readInt("Enter Employee ID: ");
        Employee emp = employeeDAO.getEmployeeByID(id);
        if (emp == null) {
            Menu.printWarning("No employee found with ID: " + id);
        } else {
            System.out.println(Menu.GREEN + "Employee Profile Found:" + Menu.RESET);
            System.out.println("  ID:         " + emp.getId());
            System.out.println("  First Name: " + emp.getFirstName());
            System.out.println("  Last Name:  " + emp.getLastName());
        }
    }

    private static void listEmployees() {
        System.out.println(Menu.CYAN + "=== Employee List ===" + Menu.RESET);
        List<Employee> list = employeeDAO.getAllEmployee();
        printEmployeesTable(list);
    }

    private static void updateEmployee(Menu menu) {
        System.out.println(Menu.CYAN + "=== Update Employee ===" + Menu.RESET);
        int id = menu.readInt("Enter Employee ID to update: ");
        Employee emp = employeeDAO.getEmployeeByID(id);
        if (emp == null) {
            Menu.printWarning("No employee found with ID: " + id);
            return;
        }

        String firstName = menu.readString("Enter new First Name (" + emp.getFirstName() + "): ");
        String lastName = menu.readString("Enter new Last Name (" + emp.getLastName() + "): ");

        emp.setFirstName(firstName);
        emp.setLastName(lastName);
        boolean success = employeeDAO.updateEmployeeByID(emp);
        if (success) {
            Menu.printSuccess("Employee updated successfully.");
        } else {
            Menu.printError("Failed to update employee.");
        }
    }

    private static void deleteEmployee(Menu menu) {
        System.out.println(Menu.CYAN + "=== Delete Employee ===" + Menu.RESET);
        int id = menu.readInt("Enter Employee ID to delete: ");
        boolean success = employeeDAO.deleteEmployeeByID(id);
        if (success) {
            Menu.printSuccess("Employee deleted successfully.");
        } else {
            Menu.printError("Failed to delete employee. ID may not exist.");
        }
    }

    private static void runAutomatedTests() {
        System.out.println(Menu.CYAN + Menu.BOLD + centerText(" RUNNING AUTOMATED TESTS (HAPPY & UNHAPPY CASES) ", 70) + Menu.RESET);
        System.out.println(Menu.CYAN + "======================================================================" + Menu.RESET);

        int passed = 0;
        int failed = 0;

        // --- HAPPY CASES ---
        System.out.println(Menu.PURPLE + "1. Happy Case: Inserting valid employee..." + Menu.RESET);
        Employee emp = new Employee("John", "Doe");
        boolean insertSuccess = employeeDAO.insertEmployee(emp);
        if (insertSuccess && emp.getId() > 0) {
            Menu.printSuccess("Passed: Inserted employee. ID generated is: " + emp.getId());
            passed++;
        } else {
            Menu.printError("Failed: Could not insert valid employee.");
            failed++;
        }
        System.out.println();

        System.out.println(Menu.PURPLE + "2. Happy Case: Retrieving employee by generated ID..." + Menu.RESET);
        Employee retrieved = employeeDAO.getEmployeeByID(emp.getId());
        if (retrieved != null && "John".equals(retrieved.getFirstName()) && "Doe".equals(retrieved.getLastName())) {
            Menu.printSuccess("Passed: Retrieved employee details match.");
            passed++;
        } else {
            Menu.printError("Failed: Retrieved employee details did not match or returned null.");
            failed++;
        }
        System.out.println();

        System.out.println(Menu.PURPLE + "3. Happy Case: Listing all employees (checking list content)..." + Menu.RESET);
        List<Employee> all = employeeDAO.getAllEmployee();
        boolean found = false;
        for (Employee e : all) {
            if (e.getId() == emp.getId()) {
                found = true;
                break;
            }
        }
        if (found) {
            Menu.printSuccess("Passed: Inserted employee was found in list of all employees.");
            passed++;
        } else {
            Menu.printError("Failed: Inserted employee was NOT found in list.");
            failed++;
        }
        System.out.println();

        System.out.println(Menu.PURPLE + "4. Happy Case: Updating employee details..." + Menu.RESET);
        retrieved.setFirstName("Jane");
        retrieved.setLastName("Smith");
        boolean updateSuccess = employeeDAO.updateEmployeeByID(retrieved);
        Employee updated = employeeDAO.getEmployeeByID(emp.getId());
        if (updateSuccess && updated != null && "Jane".equals(updated.getFirstName()) && "Smith".equals(updated.getLastName())) {
            Menu.printSuccess("Passed: Employee updated to Jane Smith successfully.");
            passed++;
        } else {
            Menu.printError("Failed: Could not update employee or updated details do not match.");
            failed++;
        }
        System.out.println();

        // --- UNHAPPY CASES ---
        System.out.println(Menu.PURPLE + "5. Unhappy Case: Retrieving non-existent employee ID (-999)..." + Menu.RESET);
        Employee nonExistent = employeeDAO.getEmployeeByID(-999);
        if (nonExistent == null) {
            Menu.printSuccess("Passed (Expected Null): Successfully handled non-existent ID gracefully.");
            passed++;
        } else {
            Menu.printError("Failed: Retrieval did not return null for a non-existent ID.");
            failed++;
        }
        System.out.println();

        System.out.println(Menu.PURPLE + "6. Unhappy Case: Updating non-existent employee..." + Menu.RESET);
        Employee mockEmp = new Employee(-999, "No", "One");
        boolean updateFailExpected = employeeDAO.updateEmployeeByID(mockEmp);
        if (!updateFailExpected) {
            Menu.printSuccess("Passed (Expected False): Prevented update of non-existent ID gracefully.");
            passed++;
        } else {
            Menu.printError("Failed: Database reported successful update of a non-existent ID.");
            failed++;
        }
        System.out.println();

        System.out.println(Menu.PURPLE + "7. Unhappy Case: Deleting non-existent employee ID (-999)..." + Menu.RESET);
        boolean deleteFailExpected = employeeDAO.deleteEmployeeByID(-999);
        if (!deleteFailExpected) {
            Menu.printSuccess("Passed (Expected False): Handled deletion failure gracefully.");
            passed++;
        } else {
            Menu.printError("Failed: Database reported successful deletion of non-existent ID.");
            failed++;
        }
        System.out.println();

        System.out.println(Menu.PURPLE + "8. Unhappy Case: Inserting null employee..." + Menu.RESET);
        boolean insertNullExpected = employeeDAO.insertEmployee(null);
        if (!insertNullExpected) {
            Menu.printSuccess("Passed (Expected False): Handled null insert gracefully.");
            passed++;
        } else {
            Menu.printError("Failed: Allowed inserting null object.");
            failed++;
        }
        System.out.println();

        System.out.println(Menu.PURPLE + "9. Unhappy Case: Database Null Constraints Violation (Null first name)..." + Menu.RESET);
        Employee invalidEmp = new Employee(null, "LastNameOnly");
        // H2 database should fail because Column First_Name is nullable=false
        boolean constraintViolationSuccess = employeeDAO.insertEmployee(invalidEmp);
        if (!constraintViolationSuccess) {
            Menu.printSuccess("Passed (Expected False): Prevented null field insertion and rolled back transaction gracefully.");
            passed++;
        } else {
            Menu.printError("Failed: Allowed null first name constraint violation.");
            failed++;
        }
        System.out.println();

        // --- CLEAN UP ---
        System.out.println(Menu.PURPLE + "10. Happy Case: Clean-up (Deleting the test employee)..." + Menu.RESET);
        boolean deleteSuccess = employeeDAO.deleteEmployeeByID(emp.getId());
        Employee checkDeleted = employeeDAO.getEmployeeByID(emp.getId());
        if (deleteSuccess && checkDeleted == null) {
            Menu.printSuccess("Passed: Clean-up complete. Test employee deleted.");
            passed++;
        } else {
            Menu.printError("Failed: Could not delete the test employee during cleanup.");
            failed++;
        }
        System.out.println();

        System.out.println(Menu.CYAN + "======================================================================" + Menu.RESET);
        System.out.println(Menu.CYAN + Menu.BOLD + String.format(" RESULTS: %d/%d Passed | %d Failed", passed, (passed + failed), failed) + Menu.RESET);
        System.out.println(Menu.CYAN + "======================================================================" + Menu.RESET);
    }

    private static void printEmployeesTable(List<Employee> list) {
        if (list == null || list.isEmpty()) {
            Menu.printWarning("No employee records available.");
            return;
        }
        System.out.println(Menu.PURPLE + "   ┌──────┬──────────────────────────────┬──────────────────────────────┐" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   │  ID  │ First Name                   │ Last Name                    │" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   ├──────┼──────────────────────────────┼──────────────────────────────┤" + Menu.RESET);
        for (Employee e : list) {
            System.out.printf(
                Menu.PURPLE + "   │" + Menu.WHITE + " %-4d " + Menu.PURPLE + "│" + Menu.WHITE + " %-28s " + Menu.PURPLE + "│" + Menu.WHITE + " %-28s " + Menu.PURPLE + "│%n" + Menu.RESET,
                e.getId(), e.getFirstName(), e.getLastName()
            );
        }
        System.out.println(Menu.PURPLE + "   └──────┴──────────────────────────────┴──────────────────────────────┘" + Menu.RESET);
    }

    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append("=");
        }
        sb.append(text);
        while (sb.length() < width) {
            sb.append("=");
        }
        return sb.toString();
    }
}
