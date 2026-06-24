package fa.training.lms.config;

import fa.training.lms.entities.CourseId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StringToCourseIdConverter implements Converter<String, CourseId> {

    @Override
    public CourseId convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }

        // Expected format: courseCode_startDate (e.g., JAVA101_2026-06-10)
        String[] parts = source.split("_");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid course ID format. Expected format: courseCode_startDate (e.g., JAVA101_2026-06-10)");
        }

        try {
            String courseCode = parts[0];
            LocalDate startDate = LocalDate.parse(parts[1]);
            return new CourseId(courseCode, startDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse course ID: " + source, e);
        }
    }
}
