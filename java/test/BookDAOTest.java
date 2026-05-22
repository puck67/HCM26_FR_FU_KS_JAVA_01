import dao.BookDAO;
import entity.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Validator;

import static org.junit.jupiter.api.Assertions.*;

public class BookDAOTest {

    private final BookDAO bookDAO = new BookDAO();
    private static final String TEST_ID = "B9999";

    @BeforeEach
    public void setUp() {
        // Delete old test book if it still exists in the database to ensure independence between tests
        bookDAO.delete(TEST_ID);
    }

    @AfterEach
    public void tearDown() {
        // Clean up test data after running each test case
        bookDAO.delete(TEST_ID);
    }

    @Test
    public void testInsertRecord() {
        Book book = new Book(TEST_ID, "Test Book Title", "author@test.com", "0123456789", 99.99, 10);
        boolean result = bookDAO.add(book);
        assertTrue(result, "Adding a new book must be successful.");
        
        Book retrieved = bookDAO.findById(TEST_ID);
        assertNotNull(retrieved, "The added book must not be null.");
        assertEquals(TEST_ID, retrieved.getId());
        assertEquals("Test Book Title", retrieved.getTitle());
    }

    @Test
    public void testFindById() {
        // Add a record before searching
        Book book = new Book(TEST_ID, "Find Me Book", "find@test.com", "0987654321", 45.0, 5);
        bookDAO.add(book);

        Book retrieved = bookDAO.findById(TEST_ID);
        assertNotNull(retrieved, "Searching for the book must find the added record.");
        assertEquals("Find Me Book", retrieved.getTitle());
        
        // Check searching for a non-existent ID
        Book nonExistent = bookDAO.findById("B0000");
        assertNull(nonExistent, "Searching for a non-existent ID must return null.");
    }

    @Test
    public void testUpdateRecord() {
        Book book = new Book(TEST_ID, "Original Title", "orig@test.com", "0123456789", 10.0, 2);
        bookDAO.add(book);

        // Update book details
        book.setTitle("Updated Title");
        book.setPrice(15.5);
        book.setQuantity(20);
        
        boolean updateResult = bookDAO.update(book);
        assertTrue(updateResult, "Updating book information must be successful.");

        Book updatedBook = bookDAO.findById(TEST_ID);
        assertNotNull(updatedBook);
        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals(15.5, updatedBook.getPrice());
        assertEquals(20, updatedBook.getQuantity());
    }

    @Test
    public void testDeleteRecord() {
        Book book = new Book(TEST_ID, "Delete Me Book", "del@test.com", "0112233445", 25.0, 1);
        bookDAO.add(book);

        boolean deleteResult = bookDAO.delete(TEST_ID);
        assertTrue(deleteResult, "Deleting the book must be successful.");

        Book retrieved = bookDAO.findById(TEST_ID);
        assertNull(retrieved, "The book must not be found after deletion.");
    }

    @Test
    public void testValidation() {
        // Check ID validation
        assertTrue(Validator.isValidBookId("B0001"));
        assertFalse(Validator.isValidBookId("A0001")); // Incorrect starting character
        assertFalse(Validator.isValidBookId("B001"));  // Missing digits
        assertFalse(Validator.isValidBookId("B00001")); // Too many digits

        // Check Email validation
        assertTrue(Validator.isValidEmail("test@gmail.com"));
        assertFalse(Validator.isValidEmail("testgmail.com")); // Missing @
        assertFalse(Validator.isValidEmail("test@"));          // Missing domain

        // Check Phone validation
        assertTrue(Validator.isValidPhone("0987654321")); // 10 digits
        assertTrue(Validator.isValidPhone("01234567890")); // 11 digits
        assertFalse(Validator.isValidPhone("012345678"));  // 9 digits (too short)
        assertFalse(Validator.isValidPhone("012345678901")); // 12 digits (too long)
        assertFalse(Validator.isValidPhone("012345abc9")); // Contains letters
    }
}
