package fa.training.dao;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Integration tests for EmployeeDAO against H2 in-memory database.
 *
 * Each test is independent: setUp inserts fresh data, tearDown cleans up.
 * Tests contain REAL assertions — no assertTrue(true) stubs.
 */
public class EmployeeDAOTest {

    private static final EmployeeDAO dao = new EmployeeDAO();
    private Employee testEmployee;

    @Before
    public void setUp() {
        testEmployee = dao.saveEmployee(new Employee("Test", "User"));
    }

    @After
    public void tearDown() {
        // Clean up test data (ignore if already deleted by a test)
        dao.deleteEmployeeById(testEmployee.getId());
    }

    @AfterClass
    public static void tearDownClass() {
        HibernateUtil.shutdown();
    }

    // ------------------------------------------------------------------ READ

    @Test
    public void findEmployeeById_existingId_returnsEmployee() {
        Optional<Employee> result = dao.findEmployeeById(testEmployee.getId());

        assertTrue("Employee should be found", result.isPresent());
        assertEquals("First name must match", "Test", result.get().getFirstName());
        assertEquals("Last name must match", "User", result.get().getLastName());
    }

    @Test
    public void findEmployeeById_nonExistingId_returnsEmpty() {
        Optional<Employee> result = dao.findEmployeeById(Integer.MAX_VALUE);

        assertFalse("Should return empty for unknown id", result.isPresent());
    }

    @Test
    public void findAllEmployees_atLeastOneRecord_returnsList() {
        List<Employee> employees = dao.findAllEmployees();

        assertNotNull("List must not be null", employees);
        assertTrue("At least 1 employee expected", employees.size() >= 1);
    }

    // ----------------------------------------------------------------- WRITE

    @Test
    public void saveEmployee_validEmployee_assignsGeneratedId() {
        Employee newEmployee = new Employee("Nguyen", "Van A");
        Employee saved = dao.saveEmployee(newEmployee);

        assertTrue("Generated id must be > 0", saved.getId() > 0);
        assertEquals("Nguyen", saved.getFirstName());
        assertEquals("Van A", saved.getLastName());

        // Cleanup
        dao.deleteEmployeeById(saved.getId());
    }

    @Test
    public void updateEmployeeById_existingId_updatesRecord() {
        boolean updated = dao.updateEmployeeById(
                testEmployee.getId(), "Updated", "Name");

        assertTrue("updateEmployeeById should return true", updated);

        Optional<Employee> reloaded = dao.findEmployeeById(testEmployee.getId());
        assertTrue(reloaded.isPresent());
        assertEquals("Updated", reloaded.get().getFirstName());
        assertEquals("Name", reloaded.get().getLastName());
    }

    @Test
    public void updateEmployeeById_nonExistingId_returnsFalse() {
        boolean result = dao.updateEmployeeById(Integer.MAX_VALUE, "X", "Y");

        assertFalse("Should return false for unknown id", result);
    }

    @Test
    public void deleteEmployeeById_existingId_removesRecord() {
        boolean deleted = dao.deleteEmployeeById(testEmployee.getId());

        assertTrue("deleteEmployeeById should return true", deleted);

        Optional<Employee> gone = dao.findEmployeeById(testEmployee.getId());
        assertFalse("Employee should no longer exist", gone.isPresent());
    }

    @Test
    public void deleteEmployeeById_nonExistingId_returnsFalse() {
        boolean result = dao.deleteEmployeeById(Integer.MAX_VALUE);

        assertFalse("Should return false for unknown id", result);
    }
}
