package fa.training.dao;

import fa.training.entities.Employee;
import java.util.List;

public interface EmployeeDao extends GenericDAO<Employee, Integer> {
    Employee getEmployeeByID(int id);
    List<Employee> getAllEmployees();
    void updateEmployeeByID(Employee employee);
    void deleteEmployeeById(int id);
    void insertEmployee(Employee employee);
}
