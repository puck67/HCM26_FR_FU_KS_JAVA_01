package com.example.lms_backend.config;

import com.example.lms_backend.entity.CourseId;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CourseIdConverterTest {

    private final CourseIdConverter converter = new CourseIdConverter();

    @Test
    void testConvertValid() {
        CourseId result = converter.convert("CS101_2026-06-10");
        assertNotNull(result);
        assertEquals("CS101", result.getCourseCode());
        assertEquals(LocalDate.of(2026, 6, 10), result.getStartDate());
    }

    @Test
    void testConvertInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("CS101-2026-06-10"));
    }

    @Test
    void testConvertInvalidDate() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("CS101_2026-13-10"));
    }
}
