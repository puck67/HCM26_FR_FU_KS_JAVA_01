package fa.training.main;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting Hibernate Employee DAO Test...");

        EmployeeDAO employeeDAO = new EmployeeDAO();

        // 1. Test Insert
        System.out.println("\n--- Testing insertEmployee ---");
        Employee emp1 = new Employee("Hung", "Phan");
        Employee emp2 = new Employee("Minh", "Nguyen");
        
        employeeDAO.insertEmployee(emp1);
        employeeDAO.insertEmployee(emp2);
        System.out.println("Inserted: " + emp1);
        System.out.println("Inserted: " + emp2);

        // 2. Test Get By ID
        System.out.println("\n--- Testing getEmployeeById ---");
        Employee fetched = employeeDAO.getEmployeeById(emp1.getId());
        System.out.println("Fetched Employee ID " + emp1.getId() + ": " + fetched);

        // 3. Test Update
        System.out.println("\n--- Testing updateEmployeeById ---");
        if (fetched != null) {
            fetched.setFirstName("Hung Updated");
            employeeDAO.updateEmployeeById(fetched);
            System.out.println("Updated: " + employeeDAO.getEmployeeById(fetched.getId()));
        }

        // 4. Test Get All (Using Java 8 Stream API)
        System.out.println("\n--- Testing getAllEmployee ---");
        List<Employee> list = employeeDAO.getAllEmployee();
        list.stream().forEach(emp -> System.out.println(" - " + emp));

        // 5. Test Delete
        System.out.println("\n--- Testing deleteEmployeeById ---");
        boolean deleted = employeeDAO.deleteEmployeeById(emp2.getId());
        System.out.println("Deleted Employee ID " + emp2.getId() + ": " + deleted);

        // Print final list to verify deletion
        System.out.println("\n--- Final Employee List ---");
        employeeDAO.getAllEmployee().stream().forEach(emp -> System.out.println(" - " + emp));

        // Shutdown SessionFactory cleanly
        HibernateUtil.shutdown();
        System.out.println("\nDatabase connection pool shut down successfully.");
    }
}
