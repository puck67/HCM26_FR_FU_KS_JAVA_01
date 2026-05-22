package fa.training.test;

import fa.training.dao.BookDAO;
import fa.training.model.Book;
import fa.training.service.BookService;
import fa.training.utils.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    private BookService service;
    private MockBookDAO mockDAO;

    // A lightweight Mock of BookDAO that runs in-memory to prevent
    // unit tests from failing when a live database is not connected.
    private static class MockBookDAO extends BookDAO {
        private final List<Book> mockDb = new ArrayList<>();

        @Override
        public boolean add(Book book) {
            if (mockDb.stream().anyMatch(b -> b.getId().equalsIgnoreCase(book.getId()))) {
                return false;
            }
            mockDb.add(book);
            return true;
        }

        @Override
        public List<Book> getAll() {
            return new ArrayList<>(mockDb);
        }

        @Override
        public Book findById(String id) {
            return mockDb.stream()
                    .filter(b -> b.getId().equalsIgnoreCase(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public boolean update(Book book) {
            for (int i = 0; i < mockDb.size(); i++) {
                if (mockDb.get(i).getId().equalsIgnoreCase(book.getId())) {
                    mockDb.set(i, book);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean delete(String id) {
            return mockDb.removeIf(b -> b.getId().equalsIgnoreCase(id));
        }
    }

    @BeforeEach
    public void setUp() {
        mockDAO = new MockBookDAO();
        service = new BookService(mockDAO);
    }

    // 1. testInsertRecord() - kiểm tra thêm dữ liệu thành công
    @Test
    public void testInsertRecord() {
        Book newBook = new Book("B001", "Clean Code", "Robert C. Martin", 
                                "unclebob@gmail.com", "0123456789", 45.0, 10, "Programming");
        
        String result = service.add(newBook);
        
        assertEquals("SUCCESS: Book added.", result);
        assertTrue(service.idExists("B001"));
        
        Book fetched = service.findById("B001");
        assertNotNull(fetched);
        assertEquals("Clean Code", fetched.getTitle());
    }

    // 2. testFindById() - kiểm tra tìm đúng record
    @Test
    public void testFindById() {
        Book book = new Book("B002", "Effective Java", "Joshua Bloch", 
                             "joshua@gmail.com", "0987654321", 50.0, 5, "Programming");
        service.add(book);
        
        Book fetched = service.findById("B002");
        
        assertNotNull(fetched);
        assertEquals("B002", fetched.getId());
        assertEquals("Effective Java", fetched.getTitle());
        
        // Find non-existent ID
        Book notFound = service.findById("B999");
        assertNull(notFound);
    }

    // 3. testUpdateRecord() - kiểm tra update thành công
    @Test
    public void testUpdateRecord() {
        Book book = new Book("B003", "Design Patterns", "Gang of Four", 
                             "gof@gmail.com", "0112233445", 55.0, 8, "Programming");
        service.add(book);
        
        String result = service.update("B003", "Design Patterns Revised", "Gang of Four", 
                                      "gof@gmail.com", "0112233445", 60.0, 12, "Technology");
        
        assertEquals("SUCCESS: Book updated.", result);
        
        Book updated = service.findById("B003");
        assertNotNull(updated);
        assertEquals("Design Patterns Revised", updated.getTitle());
        assertEquals(60.0, updated.getPrice());
        assertEquals(12, updated.getQuantity());
        assertEquals("Technology", updated.getCategory());
    }

    // 4. testDeleteRecord() - kiểm tra xóa dữ liệu
    @Test
    public void testDeleteRecord() {
        Book book = new Book("B004", "Refactoring", "Martin Fowler", 
                             "martin@gmail.com", "0555666777", 40.0, 15, "Programming");
        service.add(book);
        assertTrue(service.idExists("B004"));
        
        String result = service.delete("B004");
        
        assertEquals("SUCCESS: Book deleted.", result);
        assertFalse(service.idExists("B004"));
    }

    // 5. testValidation() - kiểm tra các phương thức validation cơ bản
    @Test
    public void testValidation() {
        // Test Email validation
        assertTrue(Validator.isValidEmail("test@example.com"));
        assertTrue(Validator.isValidEmail("john.doe@domain.vn"));
        assertFalse(Validator.isValidEmail("invalidemail"));
        assertFalse(Validator.isValidEmail("test@domain"));
        assertFalse(Validator.isValidEmail(null));
        assertFalse(Validator.isValidEmail(""));

        // Test Phone validation (7-15 digits only)
        assertTrue(Validator.isValidPhone("1234567"));
        assertTrue(Validator.isValidPhone("0987654321"));
        assertFalse(Validator.isValidPhone("123")); // too short
        assertFalse(Validator.isValidPhone("1234567890123456")); // too long
        assertFalse(Validator.isValidPhone("098-765-4321")); // contains special chars
        assertFalse(Validator.isValidPhone(null));

        // Test numeric validators
        assertTrue(Validator.isPositiveDouble(0.1));
        assertFalse(Validator.isPositiveDouble(0.0));
        assertFalse(Validator.isPositiveDouble(-5.0));

        assertTrue(Validator.isNonNegativeInt(0));
        assertTrue(Validator.isNonNegativeInt(10));
        assertFalse(Validator.isNonNegativeInt(-1));
    }

    // 6. testAddBookWithInvalidData() - kiểm tra service từ chối dữ liệu không hợp lệ
    @Test
    public void testAddBookWithInvalidData() {
        // Null book
        String result = service.add(null);
        assertTrue(result.startsWith("ERROR"));

        // Invalid email
        Book badEmail = new Book("B010", "Title", "Author",
                "invalid", "0123456789", 10.0, 1, "Programming");
        result = service.add(badEmail);
        assertTrue(result.startsWith("ERROR"));

        // Invalid phone
        Book badPhone = new Book("B011", "Title", "Author",
                "a@b.com", "123", 10.0, 1, "Programming");
        result = service.add(badPhone);
        assertTrue(result.startsWith("ERROR"));

        // Price out of range
        Book badPrice = new Book("B012", "Title", "Author",
                "a@b.com", "0123456789", -5.0, 1, "Programming");
        result = service.add(badPrice);
        assertTrue(result.startsWith("ERROR"));
    }

    // 7. testUpdateWithInvalidData() - kiểm tra update từ chối dữ liệu không hợp lệ
    @Test
    public void testUpdateWithInvalidData() {
        // First add a valid book
        Book book = new Book("B020", "Title", "Author",
                "a@b.com", "0123456789", 10.0, 1, "Science");
        service.add(book);

        // Update with invalid email
        String result = service.update("B020", "Title", "Author",
                "bad-email", "0123456789", 10.0, 1, "Science");
        assertTrue(result.startsWith("ERROR"));

        // Update non-existent book
        result = service.update("NOPE", "Title", "Author",
                "a@b.com", "0123456789", 10.0, 1, "Science");
        assertTrue(result.startsWith("ERROR"));
    }

    // 8. testDeleteWithBlankId() - kiểm tra xóa với ID rỗng
    @Test
    public void testDeleteWithBlankId() {
        String result = service.delete("");
        assertTrue(result.startsWith("ERROR"));

        result = service.delete("   ");
        assertTrue(result.startsWith("ERROR"));
    }

    // 9. testSearchWithBlankInput() - kiểm tra search với input rỗng
    @Test
    public void testSearchWithBlankInput() {
        assertTrue(service.searchById("").isEmpty());
        assertTrue(service.searchById(null).isEmpty());
        assertTrue(service.searchByTitle("").isEmpty());
        assertTrue(service.searchByTitle(null).isEmpty());
    }
}
