package fa.training.dao;

import fa.training.dao.impl.BookDAOImpl;
import fa.training.database.DBConnection;
import fa.training.model.Book;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookDAOTest {

    private static BookDAO bookDAO;
    private static Integer testAuthorId;
    private static Integer testCategoryId;
    private static final String TEST_BOOK_ID = "B999";

    @BeforeAll
    public static void setUp() throws Exception {
        bookDAO = new BookDAOImpl();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Delete old test data
            stmt.execute("DELETE FROM books WHERE id = '" + TEST_BOOK_ID + "'");
            stmt.execute("DELETE FROM authors WHERE name = 'Test Author'");
            stmt.execute("DELETE FROM categories WHERE name = 'Test Category'");

            // Insert test author
            try (ResultSet rs = stmt.executeQuery("SELECT insert_author('Test Author')")) {
                if (rs.next()) {
                    testAuthorId = rs.getInt(1);
                }
            }

            // Insert test category
            try (ResultSet rs = stmt.executeQuery("SELECT insert_category('Test Category')")) {
                if (rs.next()) {
                    testCategoryId = rs.getInt(1);
                }
            }
        }
    }

    @AfterAll
    public static void tearDown() throws Exception {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM books WHERE id = '" + TEST_BOOK_ID + "'");
            if (testAuthorId != null) {
                stmt.execute("DELETE FROM authors WHERE id = " + testAuthorId);
            }
            if (testCategoryId != null) {
                stmt.execute("DELETE FROM categories WHERE id = " + testCategoryId);
            }
        }
    }

    @Test
    @Order(1)
    public void testAddBook() {
        Book book = new Book(TEST_BOOK_ID, "Test Book Title", testAuthorId, testCategoryId, 2026, 19.99);
        boolean result = bookDAO.add(book);
        assertTrue(result, "Book should be added successfully");

        Book fetched = bookDAO.findById(TEST_BOOK_ID);
        assertNotNull(fetched, "Fetched book should not be null");
        assertEquals(TEST_BOOK_ID, fetched.getId());
        assertEquals("Test Book Title", fetched.getTitle());
        assertEquals("Test Author", fetched.getAuthorName());
        assertEquals("Test Category", fetched.getCategoryName());
        assertEquals(2026, fetched.getPublishYear());
        assertEquals(19.99, fetched.getPrice(), 0.001);
    }

    @Test
    @Order(2)
    public void testFindById() {
        Book fetched = bookDAO.findById(TEST_BOOK_ID);
        assertNotNull(fetched);
        assertEquals(TEST_BOOK_ID, fetched.getId());
    }

    @Test
    @Order(3)
    public void testGetAll() {
        List<Book> books = bookDAO.getAll();
        assertNotNull(books);
        assertFalse(books.isEmpty());
        boolean found = books.stream().anyMatch(b -> b.getId().equals(TEST_BOOK_ID));
        assertTrue(found, "The added book should be in the all books list");
    }

    @Test
    @Order(4)
    public void testUpdateBook() {
        Book book = new Book(TEST_BOOK_ID, "Updated Book Title", testAuthorId, testCategoryId, 2025, 29.99);
        boolean result = bookDAO.update(book);
        assertTrue(result, "Book should be updated successfully");

        Book fetched = bookDAO.findById(TEST_BOOK_ID);
        assertNotNull(fetched);
        assertEquals("Updated Book Title", fetched.getTitle());
        assertEquals(2025, fetched.getPublishYear());
        assertEquals(29.99, fetched.getPrice(), 0.001);
    }

    @Test
    @Order(5)
    public void testDeleteBook() {
        boolean result = bookDAO.delete(TEST_BOOK_ID);
        assertTrue(result, "Book should be deleted successfully");

        Book fetched = bookDAO.findById(TEST_BOOK_ID);
        assertNull(fetched, "Deleted book should not be found");
    }
}
