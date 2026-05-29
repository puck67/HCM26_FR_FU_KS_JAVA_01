package fa.training.controller;

import fa.training.entities.Employee;
import fa.training.service.EmployeeService;
import fa.training.view.EmployeeView;
import java.util.List;
import java.util.Scanner;

public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeView employeeView;

    public EmployeeController(EmployeeService employeeService, EmployeeView employeeView) {
        this.employeeService = employeeService;
        this.employeeView = employeeView;
    }

    public void addEmployee(Scanner scanner) {
        System.out.println("\n--- ADD NEW EMPLOYEE ---");
        try {
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine().trim();

            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine().trim();

            Employee emp = new Employee(firstName, lastName);
            employeeService.addEmployee(emp);
            System.out.println("Successfully added new employee with ID: " + emp.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            employeeView.printEmployeeList(employees);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateEmployee(Scanner scanner) {
        System.out.println("\n--- UPDATE EMPLOYEE ---");
        System.out.print("Enter Employee ID to update: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Employee existing = employeeService.getEmployee(id);

            System.out.println("Current details: " + existing);
            System.out.print("Enter new First Name (leave empty to keep unchanged): ");
            String firstName = scanner.nextLine().trim();
            if (firstName.isEmpty()) {
                firstName = existing.getFirstName();
            }

            System.out.print("Enter new Last Name (leave empty to keep unchanged): ");
            String lastName = scanner.nextLine().trim();
            if (lastName.isEmpty()) {
                lastName = existing.getLastName();
            }

            existing.setFirstName(firstName);
            existing.setLastName(lastName);
            employeeService.updateEmployee(existing);
            System.out.println("Successfully updated employee details!");
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteEmployee(Scanner scanner) {
        System.out.println("\n--- DELETE EMPLOYEE ---");
        System.out.print("Enter Employee ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Employee existing = employeeService.getEmployee(id);

            System.out.println("Employee found: " + existing.getFirstName() + " " + existing.getLastName());
            System.out.print("Are you sure you want to delete this employee? (Y/N): ");
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("Y")) {
                employeeService.deleteEmployee(id);
                System.out.println("Successfully deleted employee!");
            } else {
                System.out.println("Delete operation canceled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchEmployee(Scanner scanner) {
        System.out.println("\n--- SEARCH EMPLOYEE BY ID ---");
        System.out.print("Enter Employee ID to search: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Employee emp = employeeService.getEmployee(id);
            employeeView.printEmployeeDetails(emp);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
