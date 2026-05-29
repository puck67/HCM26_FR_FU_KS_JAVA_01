package fa.training;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Main class to test Hibernate configuration and CRUD operations.
 *
 * Problem 2: Test SessionFactory and Session connection
 * Problem 3: Create Employee object, save to DB, query back
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("=== PROBLEM 2: Test Hibernate Configuration ===");

        // Get SessionFactory (reads hibernate.cfg.xml)
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        System.out.println("Connected to fadb database successfully!");
        System.out.println("Session is open: " + session.isOpen());
        session.close();

        System.out.println("\n=== PROBLEM 3: Mapping & Saving Employee ===");

        EmployeeDAO employeeDAO = new EmployeeDAO();

        // --- INSERT ---
        System.out.println("\n--- INSERT employees ---");
        Employee emp1 = new Employee("Nguyen Van", "An");
        Employee emp2 = new Employee("Tran Thi", "Bich");
        Employee emp3 = new Employee("Le Hoang", "Nam");

        int id1 = employeeDAO.insertEmployee(emp1);
        int id2 = employeeDAO.insertEmployee(emp2);
        int id3 = employeeDAO.insertEmployee(emp3);

        // --- GET BY ID ---
        System.out.println("\n--- GET BY ID ---");
        employeeDAO.getEmployeeByID(id1);

        // --- GET ALL ---
        System.out.println("\n--- GET ALL EMPLOYEES ---");
        List<Employee> allEmployees = employeeDAO.getAllEmployees();
        for (Employee emp : allEmployees) {
            System.out.println(emp);
        }

        // --- UPDATE ---
        System.out.println("\n--- UPDATE Employee ID: " + id2 + " ---");
        employeeDAO.updateEmployeeByID(id2, "Tran Thi", "Huong");

        // --- DELETE ---
        System.out.println("\n--- DELETE Employee ID: " + id3 + " ---");
        employeeDAO.deleteEmployeeById(id3);

        // --- GET ALL AFTER CHANGES ---
        System.out.println("\n--- FINAL LIST (after update & delete) ---");
        List<Employee> finalList = employeeDAO.getAllEmployees();
        for (Employee emp : finalList) {
            System.out.println(emp);
        }

        // Shutdown
        HibernateUtil.shutdown();
        System.out.println("\nDone! Run 'SELECT * FROM EMPLOYEE' in fadb to verify.");
    }
}
