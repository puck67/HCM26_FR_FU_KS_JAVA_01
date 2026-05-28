package fa.training.dao;

import fa.training.entities.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDAO {

    Optional<Employee> findEmployeeById(int id);

    List<Employee> findAllEmployees();

    Employee insertEmployee(Employee employee);

    boolean updateEmployeeById(int id, String firstName, String lastName);

    boolean deleteEmployeeById(int id);
}
