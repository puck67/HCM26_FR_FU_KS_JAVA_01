package fa.training.dao;

import fa.training.entities.Employee;
import java.util.List;

public interface EmployeeDAO {
    
    boolean insertEmployee(Employee employee);
    
    Employee getEmployeeByID(int id);
    
    List<Employee> getAllEmployees();
    
    boolean updateEmployeeByID(int id, Employee employee);
    
    boolean deleteEmployeeByID(int id);
}
