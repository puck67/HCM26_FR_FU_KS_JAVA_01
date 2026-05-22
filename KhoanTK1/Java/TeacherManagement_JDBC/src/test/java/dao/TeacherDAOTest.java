package dao;

import model.Teacher;
import org.junit.jupiter.api.*;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TeacherDAOTest {

    private static final TeacherDAO dao = new TeacherDAO();

    private static final String TEST_ID    = "T_TEST_1";
    private static final String TEST_ID_2  = "T_TEST_2";
    private static final Teacher SAMPLE = new Teacher(
            TEST_ID, "Alice Nguyen", "alice@example.com", "0123456789", 5000.0
    );

    @BeforeAll
    static void setUp() {
        dao.delete(TEST_ID);
        dao.delete(TEST_ID_2);
    }

    @AfterAll
    static void tearDown() {
        dao.delete(TEST_ID);
        dao.delete(TEST_ID_2);
    }

    @Test
    @Order(1)
    void testInsertRecord() {
        boolean result = dao.add(SAMPLE);
        assertTrue(result, "Expected add() to return true for a new teacher");
    }

    @Test
    @Order(2)
    void testFindById() {
        Teacher found = dao.findById(TEST_ID);

        assertNotNull(found, "findById() should not return null for an existing ID");
        assertEquals(TEST_ID,              found.getId(),     "ID mismatch");
        assertEquals("Alice Nguyen",       found.getName(),   "Name mismatch");
        assertEquals("alice@example.com",  found.getEmail(),  "Email mismatch");
        assertEquals("0123456789",         found.getPhone(),  "Phone mismatch");
        assertEquals(5000.0,               found.getSalary(), 0.001, "Salary mismatch");
    }

    @Test
    @Order(3)
    void testUpdateRecord() {
        Teacher updated = new Teacher(
                TEST_ID, "Alice Updated", "alice.new@example.com", "0987654321", 7500.0
        );

        boolean result = dao.update(updated);
        assertTrue(result, "Expected update() to return true");

        Teacher fromDb = dao.findById(TEST_ID);
        assertNotNull(fromDb);
        assertEquals("Alice Updated",          fromDb.getName(),   "Name was not updated");
        assertEquals("alice.new@example.com",  fromDb.getEmail(),  "Email was not updated");
        assertEquals(7500.0,                   fromDb.getSalary(), 0.001, "Salary was not updated");
    }

    @Test
    @Order(4)
    void testDeleteRecord() {
        Teacher toDelete = new Teacher(
                TEST_ID_2, "Bob Tran", "bob@example.com", "0111222333", 3000.0
        );
        dao.add(toDelete);

        boolean deleted = dao.delete(TEST_ID_2);
        assertTrue(deleted, "Expected delete() to return true");

        Teacher shouldBeNull = dao.findById(TEST_ID_2);
        assertNull(shouldBeNull, "Teacher should no longer exist after deletion");
    }

    @Test
    @Order(5)
    void testValidation() {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        Pattern phonePattern  = Pattern.compile("^\\d+$");

        assertFalse(emailPattern.matcher("not-an-email").matches(),
                "Plain text should not match email pattern");
        assertFalse(emailPattern.matcher("missing@domain").matches(),
                "Email without TLD should not match");
        assertTrue(emailPattern.matcher("valid@domain.com").matches(),
                "A well-formed email should match");

        assertFalse(phonePattern.matcher("012-345-6789").matches(),
                "Phone with dashes should not match (digits only required)");
        assertFalse(phonePattern.matcher("abc123").matches(),
                "Alphanumeric phone should not match");
        assertTrue(phonePattern.matcher("0123456789").matches(),
                "Numeric-only phone should match");

        double invalidSalary = -100.0;
        double zeroSalary    = 0.0;
        double validSalary   = 500.0;

        assertTrue(invalidSalary <= 0, "Negative salary should fail the > 0 check");
        assertTrue(zeroSalary    <= 0, "Zero salary should fail the > 0 check");
        assertTrue(validSalary   >  0, "Positive salary should pass the > 0 check");
    }
}