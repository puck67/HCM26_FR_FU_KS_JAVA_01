package fa.training.main;

import fa.training.dao.EmployeeDao;
import fa.training.dao.EmployeeDaoImpl;
import fa.training.entities.Employee;
import fa.training.utils.HibernateUtils;
import fa.training.validation.Validator;
import java.util.List;

public class App {
    private static final EmployeeDao EMPLOYEE_DAO = new EmployeeDaoImpl();

    public static void main(String[] args) {
        System.out.println("Initializing Hibernate Session Factory...");
        try {
            HibernateUtils.getSessionFactory();
            System.out.println("Database connected smoothly.");
        } catch (Exception e) {
            System.err.println("DB Setup Error. Exit program.");
            return;
        }

        boolean keepRunning = true;
        while (keepRunning) {
            printMenuOverview();
            int choice = Validator.getValidInt("Select an option (1-6): ");

            keepRunning = switch (choice) {
                case 1 -> {
                    executeInsert();
                    yield true;
                }
                case 2 -> {
                    executeFindById();
                    yield true;
                }
                case 3 -> {
                    executeFindAll();
                    yield true;
                }
                case 4 -> {
                    executeUpdate();
                    yield true;
                }
                case 5 -> {
                    executeDelete();
                    yield true;
                }
                case 6 -> {
                    System.out.println("Terminating application... Session closed safely.");
                    HibernateUtils.shutdown();
                    yield false;
                }
                default -> {
                    System.out.println("Invalid selection. Choice must be 1 to 6.");
                    yield true;
                }
            };
        }
    }

    private static void printMenuOverview() {
        System.out.println("\n=================================================");
        System.out.println("   FSOFT TRAINING - EMPLOYEE MANAGEMENT SYSTEM   ");
        System.out.println("=================================================");
        System.out.println("1. Insert New Employee");
        System.out.println("2. Find Employee By ID");
        System.out.println("3. Display All Existing Employees");
        System.out.println("4. Update Employee Information By ID");
        System.out.println("5. Delete Employee Record By ID");
        System.out.println("6. Shutdown & Exit");
        System.out.println("=================================================");
    }

    private static void executeInsert() {
        System.out.println("\n--- Create New Record ---");
        String firstName = Validator.getValidName("First Name");
        String lastName = Validator.getValidName("Last Name");
        EMPLOYEE_DAO.insertEmployee(new Employee(firstName, lastName));
    }

    private static void executeFindById() {
        System.out.println("\n--- Search Record ---");
        int id = Validator.getValidInt("Enter Employee ID to find: ");
        Employee emp = EMPLOYEE_DAO.getEmployeeById(id);
        if (emp != null) {
            System.out.println("Result: " + emp);
        } else {
            System.out.println("No matching employee found for ID: " + id);
        }
    }

    private static void executeFindAll() {
        System.out.println("\n--- Fetching All Records (SELECT * FROM EMPLOYEE) ---");
        List<Employee> list = EMPLOYEE_DAO.getAllEmployee();
        if (list.isEmpty()) {
            System.out.println("The EMPLOYEE table is currently empty.");
        } else {
            list.forEach(System.out::println);
        }
    }

    private static void executeUpdate() {
        System.out.println("\n--- Modify Existing Record ---");
        int id = Validator.getValidInt("Enter Employee ID to update: ");
        Employee currentEmp = EMPLOYEE_DAO.getEmployeeById(id);
        if (currentEmp == null) {
            System.out.println("Update aborted. ID does not exist.");
            return;
        }
        System.out.println("Current Data: " + currentEmp);
        String newFirstName = Validator.getValidName("New First Name");
        String newLastName = Validator.getValidName("New Last Name");
        EMPLOYEE_DAO.updateEmployeeById(id, newFirstName, newLastName);
    }

    private static void executeDelete() {
        System.out.println("\n--- Remove Record ---");
        int id = Validator.getValidInt("Enter Employee ID to delete: ");
        EMPLOYEE_DAO.deleteEmployeeById(id);
    }
}
