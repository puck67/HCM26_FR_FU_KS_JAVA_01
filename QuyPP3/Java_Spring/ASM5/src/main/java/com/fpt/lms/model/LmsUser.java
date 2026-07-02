package com.fpt.lms.model;

import java.io.Serializable;

public class LmsUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private long userId;
    private String name;
    private String email;

    public LmsUser() {}

    public LmsUser(long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public void printInfo() {
        System.out.printf("👤 User - ID: %d | Name: %s | Email: %s%n", userId, name, email);
    }

    @Override
    public String toString() {
        return String.format("LmsUser[userId=%d, name='%s', email='%s']", userId, name, email);
    }

    public long getUserId() {
        return userId;
    }

    public LmsUser setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public LmsUser setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public LmsUser setEmail(String email) {
        this.email = email;
        return this;
    }
}
