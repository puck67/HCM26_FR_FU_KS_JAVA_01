package fa.training;

import fa.training.dao.EmployeeDAO;
import fa.training.dao.impl.EmployeeDAOImpl;
import fa.training.entities.Employee;
import fa.training.util.ConsoleUI;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        ConsoleUI.printBanner();

        ConsoleUI.printSection("Problem 2 · Hibernate Configuration Test");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ConsoleUI.ok("SessionFactory initialized successfully");
            ConsoleUI.detail("Database URL",
                    session.getSessionFactory().getProperties().get("hibernate.connection.url"));
            ConsoleUI.detail("Dialect", "PostgreSQLDialect (auto-detected)");
            ConsoleUI.detail("DDL auto", "update");
            ConsoleUI.ok("Session opened — connection to fadb verified");
        } catch (Exception e) {
            ConsoleUI.error("Failed to connect: " + e.getMessage());
            logger.error("SessionFactory error", e);
            HibernateUtil.shutdown();
            return;
        }

        ConsoleUI.printSection("Problem 3 · Insert New Employee");

        EmployeeDAO employeeDAO = new EmployeeDAOImpl();

        Employee newEmployee = new Employee("Nguyen", "Tuan Anh");

        try {
            Employee saved = employeeDAO.insertEmployee(newEmployee);
            ConsoleUI.ok("Employee inserted successfully");
            ConsoleUI.detail("ID", saved.getId());
            ConsoleUI.detail("First Name", saved.getFirstName());
            ConsoleUI.detail("Last Name", saved.getLastName());
            ConsoleUI.printSqlHint("SELECT * FROM employee;");
        } catch (Exception e) {
            ConsoleUI.error("Insert failed: " + e.getMessage());
            logger.error("Insert error", e);
        }

        ConsoleUI.printSection("CRUD Demo · All Operations");

        ConsoleUI.info("Fetching all employees...");
        List<Employee> all = employeeDAO.findAllEmployees();
        ConsoleUI.printEmployeeTable(all);

        if (!all.isEmpty()) {
            int targetId = all.getLast().getId();

            ConsoleUI.info("Finding employee by id=" + targetId + "...");
            Optional<Employee> found = employeeDAO.findEmployeeById(targetId);
            found.ifPresentOrElse(
                    emp -> {
                        ConsoleUI.ok("Found: " + emp);
                    },
                    () -> ConsoleUI.warn("Employee id=" + targetId + " not found."));

            ConsoleUI.divider();
            ConsoleUI.info("Updating employee id=" + targetId + " → lastName='Van A'...");
            boolean updated = employeeDAO.updateEmployeeById(targetId, "Nguyen", "Van A");
            if (updated) {
                ConsoleUI.ok("Update successful");
                employeeDAO.findEmployeeById(targetId)
                        .ifPresent(emp -> ConsoleUI.detail("After update", emp));
            } else {
                ConsoleUI.warn("Update skipped (not found)");
            }
        }

        HibernateUtil.shutdown();
        ConsoleUI.printFooter(true);
    }
}