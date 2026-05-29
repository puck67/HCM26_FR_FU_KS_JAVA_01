package fa.training.view;

import fa.training.entities.Employee;
import java.util.List;

public class EmployeeView {

    public void printEmployeeList(List<Employee> employees) {
        System.out.println("\n=================================== EMPLOYEES LIST ===================================");
        if (employees.isEmpty()) {
            System.out.println("No employees found in the system.");
            return;
        }

        String separator = "+------+-------------------------------------+-------------------------------------+";
        System.out.println(separator);
        System.out.printf("| %-4s | %-35s | %-35s |\n", "ID", "First Name", "Last Name");
        System.out.println(separator);

        employees.forEach(emp -> {
            System.out.printf("| %-4d | %-35s | %-35s |\n",
                    emp.getId(),
                    emp.getFirstName(),
                    emp.getLastName());
        });
        System.out.println(separator);
    }

    public void printEmployeeDetails(Employee emp) {
        System.out.println("\n+-----------------------------------------------------------------------------------+");
        System.out.println("|                                  EMPLOYEE DETAILS                                 |");
        System.out.println("+-----------------------------------------------------------------------------------+");
        System.out.printf("| %-15s : %-61s |\n", "Employee ID", emp.getId());
        System.out.printf("| %-15s : %-61s |\n", "First Name", emp.getFirstName());
        System.out.printf("| %-15s : %-61s |\n", "Last Name", emp.getLastName());
        System.out.println("+-----------------------------------------------------------------------------------+");
    }
}
