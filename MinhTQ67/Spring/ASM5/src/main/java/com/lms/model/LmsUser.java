package com.lms.model;

import java.io.Serializable;

/**
 * Task 1: Base class cho tất cả user trong hệ thống LMS.
 * Implements Serializable để có thể ghi/đọc từ file.
 */
public class LmsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long userId;
    protected String name;
    protected String email;

    public LmsUser(long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    /**
     * In thông tin user theo format yêu cầu:
     * "User [ID=1, Name=John Doe, Email=john.doe@fpt.com]"
     */
    public void printInfo() {
        System.out.println("User [ID=" + userId + ", Name=" + name + ", Email=" + email + "]");
    }

    @Override
    public String toString() {
        return "LmsUser [userId=" + userId + ", name=" + name + ", email=" + email + "]";
    }

    // Getters
    public long getUserId() { return userId; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
}
