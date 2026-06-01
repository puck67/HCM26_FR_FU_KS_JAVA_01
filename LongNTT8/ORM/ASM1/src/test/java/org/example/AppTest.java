package org.example;

import fa.training.entities.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testEmployeeEntityCreation() {
        Employee emp = new Employee(2, "Test", "User");
        assertNotNull(emp);
        assertEquals(2, emp.getId());
        assertEquals("Test", emp.getFirstName());
        assertEquals("User", emp.getLastName());
    }
}
