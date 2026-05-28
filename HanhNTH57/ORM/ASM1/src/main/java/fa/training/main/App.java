package fa.training.main;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.util.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class App {
    // ANSI Color constants for better UI experience (FSoft style)
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) {
        System.out.println(ANSI_CYAN + "=== STARTING HIBERNATE CONFIGURATION TEST ===" + ANSI_RESET);

        try {
            // Problem 2: Test Factory and Session
            SessionFactory factory = HibernateUtils.getSessionFactory();
            try (Session session = factory.openSession()) {
                System.out.println(ANSI_GREEN + "[OK] Successfully connected to database fadb!" + ANSI_RESET);
            }

            EmployeeDAO employeeDAO = new EmployeeDAO();

            // Problem 3: Create and Save new Employee
            System.out.println("\n--- Saving new Employee ---");
            Employee newEmployee = new Employee("Hanh", "Nguyen");
            boolean isSaved = employeeDAO.save(newEmployee);

            if (isSaved) {
                System.out.println(ANSI_GREEN + "[Success] Saved Employee: " + newEmployee + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "[Error] Failed to save Employee!" + ANSI_RESET);
            }

            // Verify: List all employees
            System.out.println("\n--- Current Employee List (SELECT * FROM EMPLOYEE) ---");
            List<Employee> employees = employeeDAO.findAll();
            if (!employees.isEmpty()) {
                employees.forEach(emp -> System.out.println(" >> " + emp));
            } else {
                System.out.println("Empty list.");
            }

        } catch (Exception e) {
            System.err.println(ANSI_RED + "[Critical Error] An error occurred during execution: " + e.getMessage() + ANSI_RESET);
            e.printStackTrace();
        } finally {
            HibernateUtils.shutdown();
        }

        System.out.println("\n" + ANSI_CYAN + "=== TEST FINISHED ===" + ANSI_RESET);
    }
}
