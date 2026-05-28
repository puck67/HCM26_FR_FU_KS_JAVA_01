package src.dao;

import org.junit.jupiter.api.*;
import src.entities.Product;
import src.utils.Validation;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductDAOTest {

    private static ProductDAO dao;

    @BeforeAll
    static void setUp() throws SQLException {
        dao = new ProductDAO();
    }

    @BeforeEach
    void cleanTestData() throws SQLException {
        dao.delete("T001");
        dao.delete("T002");
    }

    @Test
    @Order(1)
    void testInsertRecord() throws SQLException {
        Product p = new Product("T001", "Test Widget", 29.99, 5, "Gadget");
        assertTrue(dao.add(p));
        assertNotNull(dao.findById("T001"));
    }

    @Test
    @Order(2)
    void testFindById() throws SQLException {
        dao.add(new Product("T001", "Test Widget", 29.99, 5, "Gadget"));
        Product p = dao.findById("T001");
        assertNotNull(p);
        assertEquals("T001", p.getId());
        assertEquals("Test Widget", p.getName());
    }

    @Test
    @Order(3)
    void testUpdateRecord() throws SQLException {
        dao.add(new Product("T001", "Test Widget", 29.99, 5, "Gadget"));
        Product updated = new Product("T001", "Updated Widget", 49.99, 10, "Premium");
        assertTrue(dao.update(updated));
        Product found = dao.findById("T001");
        assertNotNull(found);
        assertEquals("Updated Widget", found.getName());
        assertEquals(49.99, found.getPrice(), 0.01);
    }

    @Test
    @Order(4)
    void testDeleteRecord() throws SQLException {
        dao.add(new Product("T002", "Delete Me", 9.99, 1, "Temp"));
        dao.delete("T002");
        assertNull(dao.findById("T002"));
    }

    @Test
    @Order(5)
    void testValidation() {
        // Email
        assertTrue(Validation.isValidEmail("test@example.com"));
        assertFalse(Validation.isValidEmail("notanemail"));
        assertFalse(Validation.isValidEmail("missing@dot"));
        // Phone
        assertTrue(Validation.isValidPhone("0912345678"));
        assertTrue(Validation.isValidPhone("01234567890"));
        assertFalse(Validation.isValidPhone("abc123"));
        assertFalse(Validation.isValidPhone("123"));
        // Not empty
        assertTrue(Validation.isNotEmpty("Hello"));
        assertFalse(Validation.isNotEmpty(""));
        assertFalse(Validation.isNotEmpty("   "));
        // Positive double
        assertTrue(Validation.isPositiveDouble("9.99"));
        assertFalse(Validation.isPositiveDouble("-5"));
        assertFalse(Validation.isPositiveDouble("abc"));
        // Non-negative int
        assertTrue(Validation.isNonNegativeInt("0"));
        assertTrue(Validation.isNonNegativeInt("100"));
        assertFalse(Validation.isNonNegativeInt("-1"));
    }
}
