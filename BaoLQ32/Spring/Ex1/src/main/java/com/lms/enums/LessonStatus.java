package com.lms.enums;

public enum LessonStatus {
    ACTIVE("Active"),
    DRAFT("Draft"),
    INACTIVE("Inactive");

    private final String displayName;

    LessonStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
