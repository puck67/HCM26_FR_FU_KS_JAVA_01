package com.lms.enums;

public enum ContentType {
    VIDEO("Video"),
    THEORY("Lý thuyết"),
    PRACTICE("Thực hành");

    private final String displayName;

    ContentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
