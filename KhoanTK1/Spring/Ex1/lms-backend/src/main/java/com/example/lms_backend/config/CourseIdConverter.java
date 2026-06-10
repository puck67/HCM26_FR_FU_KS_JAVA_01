package com.example.lms_backend.config;

import com.example.lms_backend.entity.CourseId;
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
        int lastUnderscore = source.lastIndexOf('_');
        if (lastUnderscore == -1) {
            throw new IllegalArgumentException("Invalid CourseId format. Use: courseCode_YYYY-MM-DD");
        }
        String courseCode = source.substring(0, lastUnderscore);
        String dateStr = source.substring(lastUnderscore + 1);
        try {
            LocalDate startDate = LocalDate.parse(dateStr);
            return new CourseId(courseCode, startDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format in CourseId. Use YYYY-MM-DD", e);
        }
    }
}
