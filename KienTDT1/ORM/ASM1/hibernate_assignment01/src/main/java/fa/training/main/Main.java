package fa.training.main;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        EmployeeDAO dao = new EmployeeDAO();

        int choice;

        do {

            System.out.println("\n========== EMPLOYEE MANAGEMENT ==========");
            System.out.println("1. Insert Employee");
            System.out.println("2. Get Employee By ID");
            System.out.println("3. Get All Employees");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:

                    System.out.print("Enter first name: ");
                    String firstName = sc.nextLine();

                    System.out.print("Enter last name: ");
                    String lastName = sc.nextLine();

                    Employee newEmployee =
                            new Employee(firstName, lastName);

                    dao.insertEmployee(newEmployee);

                    break;

                case 2:

                    System.out.print("Enter employee ID: ");
                    int findId = Integer.parseInt(sc.nextLine());

                    Employee foundEmployee =
                            dao.getEmployeeById(findId);

                    if (foundEmployee != null) {

                        System.out.println(foundEmployee);

                    } else {

                        System.out.println("Employee not found!");
                    }

                    break;

                case 3:

                    List<Employee> employees =
                            dao.getAllEmployees();

                    System.out.println("\n===== EMPLOYEE LIST =====");

                    if (employees.isEmpty()) {

                        System.out.println("No data found!");

                    } else {

                        employees.forEach(System.out::println);
                    }

                    break;

                case 4:

                    System.out.print("Enter employee ID to update: ");
                    int updateId =
                            Integer.parseInt(sc.nextLine());

                    Employee updateEmployee =
                            dao.getEmployeeById(updateId);

                    if (updateEmployee != null) {

                        System.out.print("Enter new first name: ");
                        updateEmployee.setFirstName(sc.nextLine());

                        System.out.print("Enter new last name: ");
                        updateEmployee.setLastName(sc.nextLine());

                        dao.updateEmployee(updateEmployee);

                    } else {

                        System.out.println("Employee not found!");
                    }

                    break;

                case 5:

                    System.out.print("Enter employee ID to delete: ");
                    int deleteId =
                            Integer.parseInt(sc.nextLine());

                    dao.deleteEmployeeById(deleteId);

                    break;

                case 0:

                    System.out.println("Program exited!");
                    break;

                default:

                    System.out.println("Invalid choice!");
            }

        } while (choice != 0);

        sc.close();
    }
}