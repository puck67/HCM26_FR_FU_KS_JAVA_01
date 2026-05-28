package fa.training.dao;

import fa.training.entities.Employee;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Meaningful Unit Tests for EmployeeDAO.
 * Demonstrates proper use of assertions and Java 8 patterns.
 */
public class EmployeeDAOTest {
    private EmployeeDAO employeeDAO;

    @Before
    public void setUp() {
        employeeDAO = new EmployeeDAO();
    }

    @Test
    public void testSaveAndFindById() {
        Employee emp = new Employee("Test", "User");
        boolean isSaved = employeeDAO.save(emp);
        assertTrue("Employee should be saved successfully", isSaved);

        Optional<Employee> found = employeeDAO.findById(emp.getId());
        assertTrue("Employee should be found by ID", found.isPresent());
        assertEquals("First name should match", "Test", found.get().getFirstName());
        assertEquals("Last name should match", "User", found.get().getLastName());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Employee> found = employeeDAO.findById(-999);
        assertFalse("Employee should not be found for non-existent ID", found.isPresent());
    }

    @Test
    public void testFindAll() {
        // Initial save to ensure data exists
        employeeDAO.save(new Employee("Alice", "Smith"));
        employeeDAO.save(new Employee("Bob", "Jones"));
        
        List<Employee> all = employeeDAO.findAll();
        assertNotNull("Employee list should not be null", all);
        assertTrue("Employee list should contain entries", all.size() >= 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmployeeCreation() {
        // Test defensive programming/validation
        new Employee("", "Invalid");
    }
}
