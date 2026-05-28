package fa.training.main;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;


public class App {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("      Starting Hibernate Assignment 01 Test     ");
        System.out.println("=================================================");


        System.out.println("\n[1] Verifying Hibernate Configuration...");
        SessionFactory sf = HibernateUtils.getSessionFactory();
        System.out.println(">>> SessionFactory opened successfully: " + (sf != null));

        System.out.println("\n[2] Direct Session Saving Test (Problem 3)...");
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            Employee tester = new Employee("Nguyen", "An");
            session.save(tester);
            tx.commit();
            System.out.println(">>> Direct session test insert successful: " + tester);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println(">>> Direct session saving failed: " + e.getMessage());
        }


        EmployeeDAO employeeDAO = new EmployeeDAO();
        Scanner scanner = new Scanner(System.in);
        

        boolean[] running = {true};



        Map<String, Runnable> menuActions = new HashMap<>();

        menuActions.put("1", () -> insertEmployeeAction(scanner, employeeDAO));
        menuActions.put("2", () -> viewAllEmployeesAction(employeeDAO));
        menuActions.put("3", () -> findEmployeeByIdAction(scanner, employeeDAO));
        menuActions.put("4", () -> updateEmployeeAction(scanner, employeeDAO));
        menuActions.put("5", () -> deleteEmployeeAction(scanner, employeeDAO));
        menuActions.put("6", () -> {
            System.out.println("Closing Hibernate connections and exiting program...");
            running[0] = false;
        });

        System.out.println("\n[3] Console keyboard interaction ready!");



        while (running[0]) {
            System.out.println("\n=================================================");
            System.out.println("        EMPLOYEE CRUD MANAGEMENT CONSOLE         ");
            System.out.println("=================================================");
            System.out.println("1. Insert New Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Find Employee by ID");
            System.out.println("4. Update Employee by ID");
            System.out.println("5. Delete Employee by ID");
            System.out.println("6. Exit");
            System.out.println("=================================================");
            System.out.print("Enter your choice (1-6): ");

            String choiceInput = scanner.nextLine().trim();
            menuActions.getOrDefault(choiceInput, () -> 
                System.out.println(">>> Invalid choice! Please select between 1 and 6.")
            ).run();
        }

        scanner.close();
        HibernateUtils.shutdown();
        System.out.println("\n=================================================");
        System.out.println("         APPLICATION EXITED SUCCESSFULLY         ");
        System.out.println("=================================================");
    }


    private static void insertEmployeeAction(Scanner scanner, EmployeeDAO employeeDAO) {
        System.out.println("\n--- 1. INSERT NEW EMPLOYEE ---");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println(">>> Error: First Name and Last Name cannot be empty!");
        } else {
            Employee newEmp = new Employee(firstName, lastName);
            employeeDAO.insertEmployee(newEmp);
            System.out.println(">>> Successfully added: " + newEmp);
        }
    }

    private static void viewAllEmployeesAction(EmployeeDAO employeeDAO) {
        System.out.println("\n--- 2. ALL EMPLOYEES ---");
        List<Employee> allEmployees = employeeDAO.getAllEmployee();
        if (allEmployees.isEmpty()) {
            System.out.println(">>> Database is currently empty.");
        } else {
            allEmployees.forEach(System.out::println);
        }
    }

    private static void findEmployeeByIdAction(Scanner scanner, EmployeeDAO employeeDAO) {
        System.out.println("\n--- 3. FIND EMPLOYEE BY ID ---");
        System.out.print("Enter ID to search: ");
        try {
            int searchId = Integer.parseInt(scanner.nextLine().trim());
            Optional<Employee> empOpt = employeeDAO.getEmployeeByID(searchId);
            empOpt.ifPresent(e -> System.out.println(">>> Found: " + e));
            if (!empOpt.isPresent()) {
                System.out.println(">>> No employee found with ID = " + searchId);
            }
        } catch (NumberFormatException e) {
            System.out.println(">>> Error: Please enter a valid integer ID!");
        }
    }

    private static void updateEmployeeAction(Scanner scanner, EmployeeDAO employeeDAO) {
        System.out.println("\n--- 4. UPDATE EMPLOYEE BY ID ---");
        System.out.print("Enter ID of Employee to update: ");
        try {
            int updateId = Integer.parseInt(scanner.nextLine().trim());
            Optional<Employee> empOpt = employeeDAO.getEmployeeByID(updateId);

            if (empOpt.isPresent()) {
                System.out.println(">>> Current record: " + empOpt.get());
                System.out.print("Enter New First Name: ");
                String newFirst = scanner.nextLine().trim();
                System.out.print("Enter New Last Name: ");
                String newLast = scanner.nextLine().trim();

                if (newFirst.isEmpty() || newLast.isEmpty()) {
                    System.out.println(">>> Error: New values cannot be empty!");
                } else {
                    Employee updatedDetails = new Employee(newFirst, newLast);
                    employeeDAO.updateEmployeeByID(updateId, updatedDetails);
                    System.out.println(">>> Update successful!");

                    employeeDAO.getEmployeeByID(updateId).ifPresent(e -> System.out.println(">>> New record: " + e));
                }
            } else {
                System.out.println(">>> No employee found with ID = " + updateId);
            }
        } catch (NumberFormatException e) {
            System.out.println(">>> Error: Please enter a valid integer ID!");
        }
    }

    private static void deleteEmployeeAction(Scanner scanner, EmployeeDAO employeeDAO) {
        System.out.println("\n--- 5. DELETE EMPLOYEE BY ID ---");
        System.out.print("Enter ID of Employee to delete: ");
        try {
            int deleteId = Integer.parseInt(scanner.nextLine().trim());
            Optional<Employee> empOpt = employeeDAO.getEmployeeByID(deleteId);

            if (empOpt.isPresent()) {
                System.out.println(">>> Target record: " + empOpt.get());
                System.out.print("Are you sure you want to delete this record? (Y/N): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                
                if (confirm.equals("y") || confirm.equals("yes")) {
                    employeeDAO.deleteEmployeeById(deleteId);
                    System.out.println(">>> Successfully deleted employee with ID = " + deleteId);
                } else {
                    System.out.println(">>> Deletion canceled.");
                }
            } else {
                System.out.println(">>> No employee found with ID = " + deleteId);
            }
        } catch (NumberFormatException e) {
            System.out.println(">>> Error: Please enter a valid integer ID!");
        }
    }
}
