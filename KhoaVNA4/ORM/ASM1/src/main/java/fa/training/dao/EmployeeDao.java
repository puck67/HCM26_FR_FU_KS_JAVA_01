package fa.training.dao;

import fa.training.entities.Employee;
import java.util.List;

public interface EmployeeDao {
    void insertEmployee(Employee employee);
    Employee getEmployeeById(int id);
    List<Employee> getAllEmployee();
    void updateEmployeeById(int id, String newFirstName, String newLastName);
    void deleteEmployeeById(int id);
}
