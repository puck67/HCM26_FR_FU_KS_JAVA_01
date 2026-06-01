package fa.training.daos;

import fa.training.entities.Employee;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * EmployeeDaoTest - Unit tests for EmployeeDao
 */
public class EmployeeDaoTest {
    private static EmployeeDao employeeDao;

    public EmployeeDaoTest() {
        employeeDao = new EmployeeDao();
    }

    @Test
    public void testInsertEmployee() {
        Employee employee = new Employee("John", "Doe");
        Integer id = employeeDao.insertEmployee(employee);
        assertNotNull(id);
        assertTrue(id > 0);
        System.out.println("✓ testInsertEmployee passed with ID: " + id);
    }

    @Test
    public void testGetEmployeeById() {
        Employee employee = new Employee("Jane", "Smith");
        Integer id = employeeDao.insertEmployee(employee);

        Employee retrieved = employeeDao.getEmployeeById(id);
        assertNotNull(retrieved);
        assertEquals("Jane", retrieved.getFirstName());
        assertEquals("Smith", retrieved.getLastName());
        System.out.println("✓ testGetEmployeeById passed");
    }

    @Test
    public void testGetAllEmployees() {
        for (int i = 1; i <= 3; i++) {
            Employee employee = new Employee("FN" + i, "LN" + i);
            employeeDao.insertEmployee(employee);
        }

        List<Employee> allEmployees = employeeDao.getAllEmployees();
        assertNotNull(allEmployees);
        assertTrue(allEmployees.size() >= 3);
        System.out.println("✓ testGetAllEmployees passed with " + allEmployees.size() + " employees");
    }

    @Test
    public void testUpdateEmployeeById() {
        Employee employee = new Employee("OldFirst", "OldLast");
        Integer id = employeeDao.insertEmployee(employee);

        Employee updatedDetails = new Employee("NewFirst", "NewLast");
        employeeDao.updateEmployeeById(id, updatedDetails);

        Employee result = employeeDao.getEmployeeById(id);
        assertNotNull(result);
        assertEquals("NewFirst", result.getFirstName());
        assertEquals("NewLast", result.getLastName());
        System.out.println("✓ testUpdateEmployeeById passed");
    }

    @Test
    public void testDeleteEmployeeById() {
        Employee employee = new Employee("Delete", "Me");
        Integer id = employeeDao.insertEmployee(employee);

        employeeDao.deleteEmployeeById(id);

        Employee result = employeeDao.getEmployeeById(id);
        assertNull(result);
        System.out.println("✓ testDeleteEmployeeById passed");
    }
}
