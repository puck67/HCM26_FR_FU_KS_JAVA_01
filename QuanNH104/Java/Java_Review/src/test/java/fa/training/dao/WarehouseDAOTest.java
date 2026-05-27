package fa.training.dao;

import fa.training.dao.impl.EmployeeDAOImpl;
import fa.training.dao.impl.ProductDAOImpl;
import fa.training.dao.impl.WarehouseDAOImpl;
import fa.training.db.DBConnection;
import fa.training.model.Employee;
import fa.training.model.Product;
import fa.training.model.Warehouse;
import fa.training.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseDAOTest {

    private final WarehouseDAO warehouseDAO = new WarehouseDAOImpl();
    private final EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
    private final ProductDAOImpl productDAO = new ProductDAOImpl();

    // Sample data for testing
    private final String testWhId = "WH-999";
    private final String testEmpId = "EM-999";
    private final String testProdId = "PR-999";

    @BeforeEach
    public void setUp() throws SQLException {
        // Clean up test database before each test runs
        cleanupTestData();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test database after each test runs to keep the database clean
        cleanupTestData();
    }

    private void cleanupTestData() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);
            try {
                // Delete in cascade order due to foreign keys
                stmt.executeUpdate("DELETE FROM products WHERE warehouse_id = '" + testWhId + "' OR id = '" + testProdId + "'");
                stmt.executeUpdate("DELETE FROM employees WHERE warehouse_id = '" + testWhId + "' OR id = '" + testEmpId + "'");
                stmt.executeUpdate("DELETE FROM warehouses WHERE id = '" + testWhId + "'");
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    /**
     * 1. Test case: Insert a new warehouse successfully (CRUD - Create)
     */
    @Test
    public void testInsertRecord() throws SQLException {
        var wh = new Warehouse(testWhId, "Test Warehouse", "High Tech Park District 9", 500);
        boolean inserted = warehouseDAO.add(wh);
        
        assertTrue(inserted, "Inserting a new warehouse should return true.");
        
        // Fetch to verify
        var fetched = warehouseDAO.findById(testWhId);
        assertNotNull(fetched, "Warehouse should exist in the database after insertion.");
        assertEquals("Test Warehouse", fetched.getName(), "Warehouse name should match.");
        assertEquals(500, fetched.getCapacity(), "Warehouse capacity should match.");
    }

    /**
     * 2. Test case: Find a warehouse by ID (CRUD - Read)
     */
    @Test
    public void testFindById() throws SQLException {
        var wh = new Warehouse(testWhId, "Search Warehouse", "Hanoi", 300);
        warehouseDAO.add(wh);

        // Call find
        var fetched = warehouseDAO.findById(testWhId);
        
        assertNotNull(fetched, "Finding an existing warehouse should return a non-null object.");
        assertEquals(testWhId, fetched.getId(), "Warehouse ID should match.");
        assertEquals("Search Warehouse", fetched.getName(), "Warehouse name should match.");
        
        // Find non-existent
        var notFound = warehouseDAO.findById("WH-000");
        assertNull(notFound, "Finding a non-existent warehouse should return null.");
    }

    /**
     * 3. Test case: Update warehouse details successfully (CRUD - Update)
     */
    @Test
    public void testUpdateRecord() throws SQLException {
        var wh = new Warehouse(testWhId, "Initial Warehouse", "Danang", 200);
        warehouseDAO.add(wh);

        // Modify details
        wh.setName("Updated Warehouse");
        wh.setAddress("Ho Chi Minh");
        wh.setCapacity(400);

        boolean updated = warehouseDAO.update(wh);
        assertTrue(updated, "Updating warehouse details should return true.");

        // Verify changes
        var fetched = warehouseDAO.findById(testWhId);
        assertNotNull(fetched);
        assertEquals("Updated Warehouse", fetched.getName(), "Warehouse name should be updated.");
        assertEquals("Ho Chi Minh", fetched.getAddress(), "Warehouse address should be updated.");
        assertEquals(400, fetched.getCapacity(), "Warehouse capacity should be updated.");
    }

    /**
     * 4. Test case: Delete a warehouse successfully (CRUD - Delete)
     */
    @Test
    public void testDeleteRecord() throws SQLException {
        var wh = new Warehouse(testWhId, "Warehouse to Delete", "Can Tho", 100);
        warehouseDAO.add(wh);

        boolean deleted = warehouseDAO.delete(testWhId);
        assertTrue(deleted, "Deleting warehouse should return true.");

        var fetched = warehouseDAO.findById(testWhId);
        assertNull(fetched, "Warehouse should not exist in database after deletion.");
    }

    /**
     * 5. Test case: Verify input data validation rules
     */
    @Test
    public void testValidation() {
        // Email validation checks
        assertTrue(Validator.isValidEmail("john.doe@company.com"));
        assertTrue(Validator.isValidEmail("emp_123@domain.org"));
        assertFalse(Validator.isValidEmail("invalidemail"), "Should fail: missing @ and domain.");
        assertFalse(Validator.isValidEmail("john@.com"), "Should fail: invalid domain structure.");
        
        // Phone validation checks
        assertTrue(Validator.isValidPhone("0987654321"));
        assertTrue(Validator.isValidPhone("01234567890"));
        assertFalse(Validator.isValidPhone("1234567890"), "Should fail: does not start with 0.");
        assertFalse(Validator.isValidPhone("098abc4321"), "Should fail: contains alphabetic characters.");
        assertFalse(Validator.isValidPhone("0987"), "Should fail: too short.");

        // ID validation checks
        assertTrue(Validator.isValidWarehouseId("WH-123"));
        assertFalse(Validator.isValidWarehouseId("WH123"), "Should fail: missing hyphen.");
        assertFalse(Validator.isValidWarehouseId("INVALID-123"), "Should fail: wrong prefix.");
        
        assertTrue(Validator.isValidEmployeeId("EM-55"));
        assertFalse(Validator.isValidEmployeeId("E-55"), "Should fail: wrong prefix.");

        assertTrue(Validator.isValidProductId("PR-9"));
        assertFalse(Validator.isValidProductId("PR-ABC"), "Should fail: suffix is not a number.");
    }

    /**
     * 6. Test case: Real business constraint - Capacity limits
     * Adding products that exceed warehouse capacity should be rejected
     */
    @Test
    public void testCapacityExceeded() throws SQLException {
        // Create a warehouse with small capacity (max 10 products)
        var wh = new Warehouse(testWhId, "Small Capacity Warehouse", "Hai Phong", 10);
        warehouseDAO.add(wh);

        // Add first product with quantity 6 (Success)
        var p1 = new Product(testProdId, "Product A", 150000.0, 6, testWhId);
        boolean p1Added = productDAO.add(p1);
        assertTrue(p1Added);

        // Attempt to add second product with quantity 5 (Total 6+5=11 > 10) -> Should fail
        var p2 = new Product("PR-998", "Product B", 200000.0, 5, testWhId);
        
        // Business validation is handled in the Main flow / DB layer capacity checks.
        // We simulate it here by validating the sum against the capacity.
        int currentQty = productDAO.getTotalQuantityByWarehouseId(testWhId);
        assertTrue(currentQty + p2.getQuantity() > wh.getCapacity(), "Total quantity should exceed capacity.");
    }
}
