package fa.training.dao;

import fa.training.dao.impl.VehicleDAOImpl;
import fa.training.entities.Category;
import fa.training.entities.Manufacturer;
import fa.training.entities.Vehicle;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VehicleDAOImplTest {

    private static final VehicleDAO vehicleDAO = new VehicleDAOImpl();
    private static final String TEST_ID = "V-TEST99";
    private static Vehicle testVehicle;

    @BeforeAll
    static void setUp() {
        // M01 and C01 are created in the SQL script
        Manufacturer manufacturer = new Manufacturer("M01", "Toyota", "Japan");
        Category category = new Category("C01", "Sedan", "Comfortable 4-door passenger car");
        testVehicle = new Vehicle(TEST_ID, "Test Model", 25000.0, "test@owner.com", "0987654321", manufacturer, category);

        // Delete if already exists to start clean
        vehicleDAO.delete(TEST_ID);
    }

    @AfterAll
    static void tearDown() {
        // Cleanup test data
        vehicleDAO.delete(TEST_ID);
    }

    @Test
    @Order(1)
    void testInsertRecord() {
        boolean result = vehicleDAO.add(testVehicle);
        assertTrue(result, "Vehicle should be inserted successfully.");

        Vehicle fetched = vehicleDAO.findById(TEST_ID);
        assertNotNull(fetched, "Fetched vehicle should not be null.");
        assertEquals(TEST_ID, fetched.getId());
        assertEquals("Test Model", fetched.getModel());
        assertEquals(25000.0, fetched.getPrice());
        assertEquals("test@owner.com", fetched.getOwnerEmail());
        assertEquals("0987654321", fetched.getOwnerPhone());
        assertEquals("M01", fetched.getManufacturer().getId());
        assertEquals("C01", fetched.getCategory().getId());
    }

    @Test
    @Order(2)
    void testFindById() {
        Vehicle fetched = vehicleDAO.findById(TEST_ID);
        assertNotNull(fetched);
        assertEquals(TEST_ID, fetched.getId());
        assertEquals("Test Model", fetched.getModel());
    }

    @Test
    @Order(3)
    void testUpdateRecord() {
        Vehicle fetched = vehicleDAO.findById(TEST_ID);
        assertNotNull(fetched);

        // Modify details
        fetched.setModel("Updated Model");
        fetched.setPrice(30000.0);
        fetched.setOwnerEmail("updated@owner.com");
        fetched.setOwnerPhone("0123456789");

        boolean result = vehicleDAO.update(fetched);
        assertTrue(result, "Vehicle should be updated successfully.");

        Vehicle updated = vehicleDAO.findById(TEST_ID);
        assertNotNull(updated);
        assertEquals("Updated Model", updated.getModel());
        assertEquals(30000.0, updated.getPrice());
        assertEquals("updated@owner.com", updated.getOwnerEmail());
        assertEquals("0123456789", updated.getOwnerPhone());
    }

    @Test
    @Order(4)
    void testGetAll() {
        List<Vehicle> list = vehicleDAO.getAll();
        assertNotNull(list);
        assertFalse(list.isEmpty(), "Vehicle list should not be empty.");
        boolean found = list.stream().anyMatch(v -> v.getId().equals(TEST_ID));
        assertTrue(found, "List should contain the test vehicle.");
    }

    @Test
    @Order(5)
    void testDeleteRecord() {
        boolean result = vehicleDAO.delete(TEST_ID);
        assertTrue(result, "Vehicle should be deleted successfully.");

        Vehicle fetched = vehicleDAO.findById(TEST_ID);
        assertNull(fetched, "Vehicle should no longer exist.");
    }
}
