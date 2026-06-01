package fa.training.service;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.validation.Validator;
import java.util.List;

public class EmployeeService {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public void addEmployee(Employee employee) {
        Validator.validateEmployee(employee.getFirstName(), employee.getLastName());
        employeeDAO.insertEmployee(employee);
    }

    public Employee getEmployee(int id) {
        Employee emp = employeeDAO.getEmployeeByID(id);
        if (emp == null) {
            throw new IllegalArgumentException("Employee not found with ID " + id);
        }
        return emp;
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployee();
    }

    public void updateEmployee(Employee employee) {
        Employee existing = employeeDAO.getEmployeeByID(employee.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Employee not found with ID " + employee.getId());
        }
        Validator.validateEmployee(employee.getFirstName(), employee.getLastName());
        employeeDAO.updateEmployeeByID(employee);
    }

    public void deleteEmployee(int id) {
        Employee existing = employeeDAO.getEmployeeByID(id);
        if (existing == null) {
            throw new IllegalArgumentException("Employee not found with ID " + id);
        }
        employeeDAO.deleteEmployeeByID(id);
    }
}
