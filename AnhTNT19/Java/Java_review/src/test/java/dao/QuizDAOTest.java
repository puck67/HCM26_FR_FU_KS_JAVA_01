package dao;

import database.DBConnection;
import model.Quiz;
import org.junit.jupiter.api.*;
import validation.Validator;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuizDAOTest {

    private static QuizDAO dao;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        DBConnection.setUrl(
            "jdbc:h2:mem:quiz_test;DB_CLOSE_DELAY=-1;" +
            "INIT=RUNSCRIPT FROM 'classpath:schema.sql'"
        );
        dao = new QuizDAO();
    }

    @BeforeEach
    void cleanTable() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.execute("DELETE FROM quizzes");
        }
    }

    // ----------------------------------------------------------------
    // Test 1: Insert
    // ----------------------------------------------------------------
    @Test
    @Order(1)
    void testInsertQuiz() throws SQLException {
        Quiz q = new Quiz("Q001", "What is Java?", "A programming language", 2, "Alice");
        dao.add(q);

        Quiz found = dao.findById("Q001");
        assertNotNull(found, "Inserted quiz should be found");
        assertEquals("Q001", found.getId());
        assertEquals("What is Java?", found.getQuestion());
        assertEquals(2, found.getDifficulty());
    }

    // ----------------------------------------------------------------
    // Test 2: Find by ID
    // ----------------------------------------------------------------
    @Test
    @Order(2)
    void testFindById() throws SQLException {
        dao.add(new Quiz("Q002", "Capital of Vietnam?", "Hanoi", 1, "Bob"));

        Quiz found = dao.findById("Q002");
        assertNotNull(found);
        assertEquals("Q002", found.getId());
        assertEquals("Hanoi", found.getAnswer());
        assertEquals("Bob", found.getCreatedBy());

        assertNull(dao.findById("XXXXX"));
    }

    // ----------------------------------------------------------------
    // Test 3: Update
    // ----------------------------------------------------------------
    @Test
    @Order(3)
    void testUpdateQuiz() throws SQLException {
        dao.add(new Quiz("Q003", "Old question", "Old answer", 3, "Carol"));

        Quiz updated = new Quiz("Q003", "New question", "New answer", 4, "Carol");
        dao.update(updated);

        Quiz found = dao.findById("Q003");
        assertNotNull(found);
        assertEquals("New question", found.getQuestion());
        assertEquals("New answer", found.getAnswer());
        assertEquals(4, found.getDifficulty());
    }

    // ----------------------------------------------------------------
    // Test 4: Delete
    // ----------------------------------------------------------------
    @Test
    @Order(4)
    void testDeleteQuiz() throws SQLException {
        dao.add(new Quiz("Q004", "Temp question?", "Temp answer", 1, "Dave"));

        dao.delete("Q004");

        assertNull(dao.findById("Q004"), "Deleted quiz should not be found");
    }

    // ----------------------------------------------------------------
    // Test 5: Validation
    // ----------------------------------------------------------------
    @Test
    @Order(5)
    void testValidation() {
        assertTrue(Validator.isValidDifficulty(1));
        assertTrue(Validator.isValidDifficulty(5));
        assertFalse(Validator.isValidDifficulty(0));
        assertFalse(Validator.isValidDifficulty(6));

        assertTrue(Validator.isNotEmpty("Hello"));
        assertFalse(Validator.isNotEmpty(""));
        assertFalse(Validator.isNotEmpty("   "));
        assertFalse(Validator.isNotEmpty(null));

        assertTrue(Validator.isValidIdFormat("Q001"));
        assertTrue(Validator.isValidIdFormat("AB"));
        assertFalse(Validator.isValidIdFormat(""));
        assertFalse(Validator.isValidIdFormat("A"));
        assertFalse(Validator.isValidIdFormat("ABCDE12345X"));

        assertEquals(3, Validator.parseIntSafe("3"));
        assertEquals(-1, Validator.parseIntSafe("abc"));
    }

    // ----------------------------------------------------------------
    // Test 6: Get All
    // ----------------------------------------------------------------
    @Test
    @Order(6)
    void testGetAll() throws SQLException {
        dao.add(new Quiz("Q010", "Q1?", "A1", 1, "Eve"));
        dao.add(new Quiz("Q011", "Q2?", "A2", 2, "Eve"));
        dao.add(new Quiz("Q012", "Q3?", "A3", 3, "Eve"));

        List<Quiz> list = dao.getAll();
        assertEquals(3, list.size());
    }
}
