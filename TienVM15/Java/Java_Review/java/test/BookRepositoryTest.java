import repository.BookRepository;
import repository.BookRepositoryImpl;
import model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import validation.InputValidator;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryTest {

    private final BookRepository bookRepository = new BookRepositoryImpl();
    private static final String TEST_ID = "B9999";

    @BeforeEach
    public void setUp() {
        // Delete old test book if it still exists in the database to ensure independence between tests
        bookRepository.delete(TEST_ID);
    }

    @AfterEach
    public void tearDown() {
        // Clean up test data after running each test case
        bookRepository.delete(TEST_ID);
    }

    @Test
    public void testInsertRecord() {
        Book book = new Book(TEST_ID, "Test Book Title", "author@test.com", "0123456789", 99.99, 10);
        boolean result = bookRepository.add(book);
        assertTrue(result, "Adding a new book must be successful.");
        
        Book retrieved = bookRepository.findById(TEST_ID);
        assertNotNull(retrieved, "The added book must not be null.");
        assertEquals(TEST_ID, retrieved.getId());
        assertEquals("Test Book Title", retrieved.getTitle());
    }

    @Test
    public void testFindById() {
        // Add a record before searching
        Book book = new Book(TEST_ID, "Find Me Book", "find@test.com", "0987654321", 45.0, 5);
        bookRepository.add(book);

        Book retrieved = bookRepository.findById(TEST_ID);
        assertNotNull(retrieved, "Searching for the book must find the added record.");
        assertEquals("Find Me Book", retrieved.getTitle());
        
        // Check searching for a non-existent ID
        Book nonExistent = bookRepository.findById("B0000");
        assertNull(nonExistent, "Searching for a non-existent ID must return null.");
    }

    @Test
    public void testUpdateRecord() {
        Book book = new Book(TEST_ID, "Original Title", "orig@test.com", "0123456789", 10.0, 2);
        bookRepository.add(book);

        // Update book details
        book.setTitle("Updated Title");
        book.setPrice(15.5);
        book.setQuantity(20);
        
        boolean updateResult = bookRepository.update(book);
        assertTrue(updateResult, "Updating book information must be successful.");

        Book updatedBook = bookRepository.findById(TEST_ID);
        assertNotNull(updatedBook);
        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals(15.5, updatedBook.getPrice());
        assertEquals(20, updatedBook.getQuantity());
    }

    @Test
    public void testDeleteRecord() {
        Book book = new Book(TEST_ID, "Delete Me Book", "del@test.com", "0112233445", 25.0, 1);
        bookRepository.add(book);

        boolean deleteResult = bookRepository.delete(TEST_ID);
        assertTrue(deleteResult, "Deleting the book must be successful.");

        Book retrieved = bookRepository.findById(TEST_ID);
        assertNull(retrieved, "The book must not be found after deletion.");
    }

    @Test
    public void testValidation() {
        // Check ID validation
        assertTrue(InputValidator.isValidBookId("B0001"));
        assertFalse(InputValidator.isValidBookId("A0001")); // Incorrect starting character
        assertFalse(InputValidator.isValidBookId("B001"));  // Missing digits
        assertFalse(InputValidator.isValidBookId("B00001")); // Too many digits

        // Check Email validation
        assertTrue(InputValidator.isValidEmail("test@gmail.com"));
        assertFalse(InputValidator.isValidEmail("testgmail.com")); // Missing @
        assertFalse(InputValidator.isValidEmail("test@"));          // Missing domain

        // Check Phone validation
        assertTrue(InputValidator.isValidPhone("0987654321")); // 10 digits
        assertTrue(InputValidator.isValidPhone("01234567890")); // 11 digits
        assertFalse(InputValidator.isValidPhone("012345678"));  // 9 digits (too short)
        assertFalse(InputValidator.isValidPhone("012345678901")); // 12 digits (too long)
        assertFalse(InputValidator.isValidPhone("012345abc9")); // Contains letters
    }
}
