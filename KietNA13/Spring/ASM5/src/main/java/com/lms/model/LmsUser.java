package com.lms.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class cho tất cả user trong hệ thống LMS.
 * Implements Serializable để hỗ trợ ghi/đọc file qua ObjectOutputStream.
 */
public class LmsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final long userId;
    protected final String name;
    protected final String email;

    public LmsUser(long userId, String name, String email) {
        if (userId <= 0) throw new IllegalArgumentException("userId must be positive, got: " + userId);
        this.userId = userId;
        this.name = requireNonBlank(name, "name");
        this.email = requireNonBlank(email, "email");
    }

    protected static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Field '" + field + "' must not be blank");
        }
        return value.strip();
    }

    /**
     * In thông tin user theo format:
     * "User [ID=1, Name=John Doe, Email=john.doe@fpt.com]"
     */
    public void displayUserInfo() {
        System.out.printf("User [ID=%d, Name=%s, Email=%s]%n", userId, name, email);
    }

    // ── Getters ────────────────────────────────────────────────────────────────
    public long getUserId() { return userId; }
    public String getName()  { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return String.format("LmsUser[userId=%d, name='%s', email='%s']", userId, name, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LmsUser)) return false;
        return userId == ((LmsUser) o).userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
