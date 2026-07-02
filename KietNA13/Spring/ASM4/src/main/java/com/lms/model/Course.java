package com.lms.model;

/**
 * Immutable domain model representing a single LMS course.
 * All fields are declared {@code final} to enforce immutability after construction.
 * Use the all-args constructor; setters are intentionally omitted.
 */
public class Course {

    // Unique identifier for this course (e.g., "C001")
    private final String courseId;

    // Human-readable title of the course
    private final String title;

    // Total duration of the course expressed in hours
    private final int durationHours;

    // Full name of the instructor delivering the course
    private final String instructorName;

    /**
     * Constructs a fully-initialised, immutable {@code Course}.
     *
     * @param courseId       unique course identifier — must not be null or blank
     * @param title          course title — must not be null or blank
     * @param durationHours  total duration in hours — must be positive
     * @param instructorName name of the instructor — must not be null or blank
     * @throws IllegalArgumentException if any validation guard fails
     */
    public Course(String courseId, String title, int durationHours, String instructorName) {

        if (courseId == null || courseId.isBlank()) {
            throw new IllegalArgumentException("courseId must not be null or blank");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be null or blank");
        }

        if (durationHours <= 0) {
            throw new IllegalArgumentException("durationHours must be a positive integer, got: " + durationHours);
        }

        if (instructorName == null || instructorName.isBlank()) {
            throw new IllegalArgumentException("instructorName must not be null or blank");
        }

        this.courseId       = courseId;
        this.title          = title;
        this.durationHours  = durationHours;
        this.instructorName = instructorName;
    }

    // -------------------------------------------------------------------------
    // Accessors (read-only — no setters on an immutable model)
    // -------------------------------------------------------------------------

    /** Returns the unique course identifier. */
    public String getCourseId() { return courseId; }

    /** Returns the course title. */
    public String getTitle() { return title; }

    /** Returns the total duration of the course in hours. */
    public int getDurationHours() { return durationHours; }

    /** Returns the full name of the instructor. */
    public String getInstructorName() { return instructorName; }

    /**
     * Returns a concise, human-readable representation of this course,
     * useful for logging and console output.
     */
    @Override
    public String toString() {
        return "Course{id='" + courseId
                + "', title='" + title
                + "', duration=" + durationHours + "h"
                + ", instructor='" + instructorName + "'}";
    }
}
