package com.lms.model;

import java.io.Serializable;

public class LmsUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private long userId;
    private String name;
    private String email;

    public LmsUser(long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public void printInfo() {
        System.out.println("User [ID=" + userId + ", Name=" + name + ", Email=" + email + "]");
    }

    @Override
    public String toString() {
        return "LmsUser [userId=" + userId + ", name=" + name + ", email=" + email + "]";
    }

    // Getters and Setters
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
