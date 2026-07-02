package com.lms.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model đại diện cho một Course trong hệ thống LMS.
 * Implement Serializable để hỗ trợ persistence bằng ObjectOutputStream.
 *
 * serialVersionUID cố định — nếu thay đổi cấu trúc class mà không cập nhật UID,
 * các file .dat cũ sẽ không deserialize được (gây InvalidClassException).
 */
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String title;
    private final String instructorName;
    private final String description;
    private final int durationHours;

    public Course(String title, String instructorName, String description, int durationHours) {

        this.title = requireNonBlank(title, "title");
        this.instructorName = requireNonBlank(instructorName, "instructorName");
        this.description = requireNonBlank(description, "description");
        if (durationHours < 1 || durationHours > 1000) {
            throw new IllegalArgumentException(
                    "durationHours must be between 1 and 1000, got: " + durationHours);
        }
        this.durationHours = durationHours;
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Field '" + fieldName + "' must not be blank");
        }
        return value.strip();
    }

    /** In thông tin Course ra console */
    public void displayCourseInfo() {
        System.out.printf("[Course: %s] by %s (%d hours): %s%n",
                title, instructorName, durationHours, description);
    }

    // ── Getters (immutable — không có setter) ─────────────────────────────────

    public String getTitle() { return title; }
    public String getInstructorName() { return instructorName; }
    public String getDescription() { return description; }
    public int getDurationHours() { return durationHours; }

    @Override
    public String toString() {
        return String.format("Course[title='%s', instructor='%s', duration=%d h]",
                title, instructorName, durationHours);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return durationHours == course.durationHours
                && Objects.equals(title, course.title)
                && Objects.equals(instructorName, course.instructorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, instructorName, durationHours);
    }
}
