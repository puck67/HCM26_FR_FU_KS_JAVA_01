package com.lms.converter;

import com.lms.entity.CourseId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CourseIdConverter implements Converter<String, CourseId> {

    @Override
    public CourseId convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        String[] parts = source.split("_", 2);
        if (parts.length == 2) {
            try {
                return new CourseId(parts[0], LocalDate.parse(parts[1]));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format in CourseId: " + parts[1]);
            }
        }
        throw new IllegalArgumentException("Invalid CourseId format. Expected 'courseCode_startDate', got: " + source);
    }
}
