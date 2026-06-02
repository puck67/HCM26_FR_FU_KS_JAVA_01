package org.example.service;

import org.example.dao.StudentDAO;
import org.example.model.Student;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StudentService}.
 *
 * Uses Java 8 features: Optional assertions, lambda matchers, method references.
 * {@link StudentDAO} is mocked — no database required.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Unit Tests")
class StudentServiceTest {

    @Mock
    private StudentDAO mockDao;

    @InjectMocks
    private StudentService service;

    // ─── Sample data factory ─────────────────────────────────────────────────────

    private Student validStudent() {
        return new Student("S001", "Nguyen Van A", "a@example.com", "0901234567", 3.5);
    }

    // ─── Test 1: Add – success ────────────────────────────────────────────────────

    @Test
    @DisplayName("1. addStudent - should insert successfully when data is valid and ID is unique")
    void testAddStudentSuccess() {
        Student s = validStudent();

        when(mockDao.findById("S001")).thenReturn(Optional.empty());
        when(mockDao.add(s)).thenReturn(true);

        String result = service.addStudent(s);

        assertEquals("Student added successfully.", result);
        verify(mockDao, times(1)).add(s);
    }

    // ─── Test 2: Add – duplicate ID ──────────────────────────────────────────────

    @Test
    @DisplayName("2. addStudent - should reject duplicate ID")
    void testAddStudentDuplicateId() {
        Student s = validStudent();

        when(mockDao.findById("S001")).thenReturn(Optional.of(s));

        String result = service.addStudent(s);

        assertTrue(result.contains("already exists"), "Should report duplicate ID");
        verify(mockDao, never()).add(any());
    }

    // ─── Test 3: findById – found ─────────────────────────────────────────────────

    @Test
    @DisplayName("3. findById - should return correct student when ID exists")
    void testFindByIdFound() {
        Student expected = validStudent();
        when(mockDao.findById("S001")).thenReturn(Optional.of(expected));

        // Use Optional.isPresent + get with Java 8 style assertions
        Optional<Student> result = service.findById("S001");

        assertTrue(result.isPresent(), "Should find the student");
        result.ifPresent(s -> {
            assertEquals("S001",          s.getId());
            assertEquals("Nguyen Van A",  s.getName());
            assertEquals("a@example.com", s.getEmail());
            assertEquals("0901234567",    s.getPhone());
            assertEquals(3.5,             s.getGpa(), 0.001);
        });
    }

    // ─── Test 4: Update – success ─────────────────────────────────────────────────

    @Test
    @DisplayName("4. updateStudent - should update successfully when student exists and data is valid")
    void testUpdateStudentSuccess() {
        Student original = validStudent();
        Student updated  = new Student("S001", "Nguyen Van B", "b@example.com", "0987654321", 3.8);

        when(mockDao.findById("S001")).thenReturn(Optional.of(original));
        when(mockDao.update(updated)).thenReturn(true);

        String result = service.updateStudent(updated);

        assertEquals("Student updated successfully.", result);
        verify(mockDao, times(1)).update(updated);
    }

    // ─── Test 5: Delete – success ─────────────────────────────────────────────────

    @Test
    @DisplayName("5. deleteStudent - should delete successfully when student exists")
    void testDeleteStudentSuccess() {
        Student s = validStudent();

        when(mockDao.findById("S001")).thenReturn(Optional.of(s));
        when(mockDao.delete("S001")).thenReturn(true);

        String result = service.deleteStudent("S001");

        assertEquals("Student deleted successfully.", result);
        verify(mockDao, times(1)).delete("S001");
    }

    // ─── Test 6: Delete – not found ───────────────────────────────────────────────

    @Test
    @DisplayName("6. deleteStudent - should report not-found when ID does not exist")
    void testDeleteStudentNotFound() {
        when(mockDao.findById("S999")).thenReturn(Optional.empty());

        String result = service.deleteStudent("S999");

        assertTrue(result.contains("not found"), "Should report student not found");
        verify(mockDao, never()).delete(any());
    }

    // ─── Test 7: Validation – invalid email ───────────────────────────────────────

    @Test
    @DisplayName("7. validateStudent - should reject invalid email format")
    void testValidationInvalidEmail() {
        Student s = new Student("S002", "Test User", "not-an-email", "0901234567", 3.0);

        // Use Optional assertion (Java 8 style)
        Optional<String> error = service.validateStudent(s);

        assertTrue(error.isPresent(), "Should return a validation error");
        error.ifPresent(msg ->
            assertTrue(msg.toLowerCase().contains("email"), "Error should mention 'email'")
        );
    }

    // ─── Test 8: Validation – invalid phone ───────────────────────────────────────

    @Test
    @DisplayName("8. validateStudent - should reject phone with letters")
    void testValidationInvalidPhone() {
        Student s = new Student("S003", "Test User", "user@test.com", "ABC123", 2.5);

        Optional<String> error = service.validateStudent(s);

        assertTrue(error.isPresent(), "Should return a validation error");
        error.ifPresent(msg ->
            assertTrue(msg.toLowerCase().contains("phone"), "Error should mention 'phone'")
        );
    }

    // ─── Test 9: Validation – GPA out of range ────────────────────────────────────

    @Test
    @DisplayName("9. validateStudent - should reject GPA > 4.0")
    void testValidationGpaOutOfRange() {
        Student s = new Student("S004", "Test User", "user@test.com", "0901234567", 5.0);

        Optional<String> error = service.validateStudent(s);

        assertTrue(error.isPresent(), "Should return a validation error for GPA > 4.0");
        error.ifPresent(msg ->
            assertTrue(msg.toLowerCase().contains("gpa"), "Error should mention 'gpa'")
        );
    }

    // ─── Test 10: getAllStudents ───────────────────────────────────────────────────

    @Test
    @DisplayName("10. getAllStudents - should return all students sorted by ID")
    void testGetAllStudents() {
        List<Student> mockList = Arrays.asList(
                new Student("S003", "Charlie", "charlie@test.com", "0903333333", 3.5),
                new Student("S001", "Alice",   "alice@test.com",   "0901111111", 3.8),
                new Student("S002", "Bob",     "bob@test.com",     "0902222222", 2.9)
        );
        when(mockDao.getAll()).thenReturn(mockList);

        List<Student> result = service.getAllStudents();

        assertEquals(3, result.size());
        // Verify sorted by ID using stream
        assertEquals("S001", result.get(0).getId());
        assertEquals("S002", result.get(1).getId());
        assertEquals("S003", result.get(2).getId());
    }
}
