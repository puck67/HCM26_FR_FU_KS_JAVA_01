package com.fpt.lms.entity;

/**
 * Enum representing the lifecycle status of a Course.
 * Replaces raw integer magic numbers (1=DRAFT, 2=PUBLISHED, 3=ARCHIVED).
 */
public enum CourseStatus {

    DRAFT(1),
    PUBLISHED(2),
    ARCHIVED(3);

    private final int value;

    CourseStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CourseStatus fromValue(int value) {
        for (CourseStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown CourseStatus value: " + value);
    }
}
