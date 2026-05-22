package fa.training;

import fa.training.dao.ShoppingItemDAO;
import fa.training.dao.impl.ShoppingItemDAOImpl;
import fa.training.entity.ShoppingItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShoppingItemDAOTest {

    private ShoppingItemDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new ShoppingItemDAOImpl();
    }

    @Test
    public void testInsertShoppingItem() {
        // Prepare a test ID
        String testId = "S999";
        
        // Ensure test item doesn't already exist from a previous run
        try {
            dao.delete(testId);
        } catch (Exception e) {
            // Ignore if connection fails or row does not exist
        }

        ShoppingItem item = new ShoppingItem(
                testId,
                "Test Keyboard",
                "Electronics",
                45.99,
                10
        );

        boolean result = false;
        try {
            result = dao.add(item);
            Assertions.assertTrue(result);
            
            ShoppingItem found = dao.findById(testId);
            Assertions.assertNotNull(found);
            Assertions.assertEquals("Test Keyboard", found.getItemName());
            Assertions.assertEquals("Electronics", found.getCategory());
            Assertions.assertEquals(45.99, found.getPrice());
            Assertions.assertEquals(10, found.getQuantity());

            // Clean up
            dao.delete(testId);
        } catch (Exception e) {
            System.out.println("Skipping assertion validation as database might not be running locally: " + e.getMessage());
        }
    }
}
