package fa.training.service;

import fa.training.dao.EmployeeDAO;
import fa.training.dao.impl.EmployeeDAOImpl;
import fa.training.entities.Employee;
import java.util.List;
import java.util.Optional;

public class EmployeeService {

    private final EmployeeDAO employeeDAO = new EmployeeDAOImpl();

    public List<Employee> getAllEmployees() {
        return employeeDAO.findAllEmployees();
    }

    public Optional<Employee> getEmployeeById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID phai lon hon 0.");
        }
        return employeeDAO.findEmployeeById(id);
    }

    public Employee addEmployee(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten (First Name) khong duoc de trong.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Ho (Last Name) khong duoc de trong.");
        }
        if (firstName.length() > 50 || lastName.length() > 50) {
            throw new IllegalArgumentException("Ten hoac ho khong duoc vuot qua 50 ky tu.");
        }
        
        Employee emp = new Employee(firstName.trim(), lastName.trim());
        return employeeDAO.insertEmployee(emp);
    }

    public boolean updateEmployee(int id, String firstName, String lastName) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID phai lon hon 0.");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten (First Name) khong duoc de trong.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Ho (Last Name) khong duoc de trong.");
        }
        if (firstName.length() > 50 || lastName.length() > 50) {
            throw new IllegalArgumentException("Ten hoac ho khong duoc vuot qua 50 ky tu.");
        }

        return employeeDAO.updateEmployeeById(id, firstName.trim(), lastName.trim());
    }

    public boolean deleteEmployee(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID phai lon hon 0.");
        }
        return employeeDAO.deleteEmployeeById(id);
    }
}
