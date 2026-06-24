package com.example.demo;

import com.example.demo.model.Student;
import com.example.demo.model.Teacher;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CrudApplicationTests {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void contextLoads() {
        assertTrue(validator != null);
    }

    @org.springframework.beans.factory.annotation.Autowired
    private com.example.demo.repository.TeacherRepository teacherRepository;

    @Test
    void testGetRowsHasId() {
        Teacher teacher = teacherRepository.save(new Teacher(null, "Alice Smith", "alice.smith@example.com", "Physics", 5500.0));
        java.util.List<Teacher> list = java.util.List.of(teacher);
        java.util.List<java.util.Map<String, Object>> rows = com.example.demo.util.GenericTableHelper.getRows(list);
        assertFalse(rows.isEmpty());
        Object idVal = rows.get(0).get("rowId");
        assertTrue(idVal != null);
    }

    @Test
    void testTeacherValid() {
        Teacher teacher = new Teacher(null, "John Doe", "john.doe@example.com", "Maths", 5000.0);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testTeacherInvalidName() {
        Teacher teacher = new Teacher(null, "John123", "john.doe@example.com", "Maths", 5000.0);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testTeacherInvalidEmail() {
        Teacher teacher = new Teacher(null, "John Doe", "invalid-email", "Maths", 5000.0);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testTeacherNegativeSalary() {
        Teacher teacher = new Teacher(null, "John Doe", "john@example.com", "Maths", -100.0);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testStudentValid() {
        Student student = new Student(null, "Jane Doe", "jane@example.com", 9.5, "Class-A");
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testStudentInvalidGpaHigh() {
        Student student = new Student(null, "Jane Doe", "jane@example.com", 11.0, "Class-A");
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testStudentInvalidGpaLow() {
        Student student = new Student(null, "Jane Doe", "jane@example.com", -1.0, "Class-A");
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testStudentInvalidClassroom() {
        Student student = new Student(null, "Jane Doe", "jane@example.com", 8.0, "Class_A!");
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testExcelExportAndImport() throws Exception {
        Teacher t1 = new Teacher(null, "Test Teacher", "test.teacher@example.com", "Computer Science", 4500.0);
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        com.example.demo.util.ExcelHelper.exportToExcel(bos, java.util.List.of(t1), Teacher.class);
        byte[] bytes = bos.toByteArray();
        assertTrue(bytes.length > 0);

        java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bytes);
        java.util.List<Teacher> imported = com.example.demo.util.ExcelHelper.importFromExcel(bis, Teacher.class);
        assertFalse(imported.isEmpty());
        Teacher importedTeacher = imported.get(0);
        assertTrue("Test Teacher".equals(importedTeacher.getName()));
        assertTrue("test.teacher@example.com".equals(importedTeacher.getEmail()));
        assertTrue("Computer Science".equals(importedTeacher.getSubject()));
        assertTrue(4500.0 == importedTeacher.getSalary());
    }

    @Test
    void testExcelImportInvalidTemplate() {
        java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(new byte[0]);
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            com.example.demo.util.ExcelHelper.importFromExcel(bis, Teacher.class);
        });
    }
}
