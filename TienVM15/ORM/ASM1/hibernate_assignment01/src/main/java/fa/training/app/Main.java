package fa.training.app;

import fa.training.dao.EmployeeDao;
import fa.training.dao.impl.EmployeeDaoImpl;
import fa.training.entities.Employee;
import fa.training.util.HibernateUtil;
import fa.training.util.TableRenderer;

import java.util.*;

public class Main {

    private static final EmployeeDao employeeDao = new EmployeeDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);
    private static final MenuManager menu = new MenuManager(scanner);

    public static void main(String[] args) {
        System.out.println("Initializing SessionFactory and connecting to 'fadb' database...");
        try {
            HibernateUtil.getSessionFactory();
            System.out.println("Database Connection initialized successfully.");
        } catch (Exception e) {
            System.err.println("Database Connection failed. Make sure MySQL server is running and database 'fadb' exists.");
            e.printStackTrace();
            System.exit(1);
        }

        // PDF Problem 3 requirement:
        // "Create a new Employee object with your selected first name and last name on the main method.
        // Use the existing session to save new Employee object."
        seedInitialEmployee();

        runMainMenu();
    }

    private static void seedInitialEmployee() {
        try {
            List<Employee> list = employeeDao.getAllEmployees();
            if (list.isEmpty()) {
                System.out.println("[Seeding] Database is empty. Creating initial Employee as required by PDF...");
                Employee emp = new Employee("John", "Doe");
                employeeDao.insertEmployee(emp);
                System.out.println("✅ Initial Employee created: " + emp);
            }
        } catch (Exception e) {
            System.err.println("[Seeding] Failed: " + e.getMessage());
        }
    }

    private static void runMainMenu() {
        Map<Integer, String> descriptions = new LinkedHashMap<>();
        Map<Integer, MenuAction> actions = new LinkedHashMap<>();

        descriptions.put(1, "Create a new Employee");
        descriptions.put(2, "Update an Employee");
        descriptions.put(3, "Delete an Employee");
        descriptions.put(4, "List All Employees");
        descriptions.put(0, "Exit App");

        actions.put(1, () -> {
            String firstName = menu.readStringWithLengthLimit("Enter first name (max 50 chars): ", 50);
            String lastName = menu.readStringWithLengthLimit("Enter last name (max 50 chars): ", 50);
            Employee emp = new Employee(firstName, lastName);
            employeeDao.insertEmployee(emp);
            System.out.println("✅ Employee created successfully! ID is: " + emp.getId());
        });

        actions.put(2, () -> {
            if (!printEmployeesSummary()) return;
            int id = menu.readInt("Enter Employee ID to update: ");
            Employee emp = employeeDao.getEmployeeByID(id);
            if (emp != null) {
                String firstName = menu.readStringWithLengthLimit("Enter new first name (max 50 chars): ", 50);
                String lastName = menu.readStringWithLengthLimit("Enter new last name (max 50 chars): ", 50);
                emp.setFirstName(firstName);
                emp.setLastName(lastName);
                employeeDao.updateEmployeeByID(emp);
                System.out.println("✅ Employee updated successfully!");
            } else {
                System.out.println("❌ Employee not found.");
            }
        });

        actions.put(3, () -> {
            if (!printEmployeesSummary()) return;
            int id = menu.readInt("Enter Employee ID to delete: ");
            Employee emp = employeeDao.getEmployeeByID(id);
            if (emp != null) {
                employeeDao.deleteEmployeeById(id);
                System.out.println("✅ Employee deleted successfully!");
            } else {
                System.out.println("❌ Employee not found.");
            }
        });

        actions.put(4, () -> {
            printEmployeesSummary();
        });

        actions.put(0, () -> {
            System.out.println("Closing SessionFactory and exiting. Goodbye!");
            HibernateUtil.shutdown();
            System.exit(0);
        });

        menu.runMenu("EMPLOYEE MANAGEMENT SYSTEM", descriptions, actions);
    }

    private static boolean printEmployeesSummary() {
        List<Employee> list = employeeDao.getAllEmployees();
        if (list.isEmpty()) {
            System.out.println("⚠️ [Notice] No employees found.");
            return false;
        }

        List<String> headers = List.of("Employee ID", "First Name", "Last Name");
        List<List<String>> rows = new ArrayList<>();
        for (Employee emp : list) {
            rows.add(List.of(
                String.valueOf(emp.getId()),
                emp.getFirstName(),
                emp.getLastName()
            ));
        }

        TableRenderer.printTable("Employee Directory", headers, rows);
        return true;
    }
}
