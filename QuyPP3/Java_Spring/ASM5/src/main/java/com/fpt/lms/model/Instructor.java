package com.fpt.lms.model;

public class Instructor extends LmsUser {
    private static final long serialVersionUID = 1L;
    private String department;
    private String bio;

    public Instructor() {}

    public Instructor(long userId, String name, String email, String department, String bio) {
        super(userId, name, email);
        this.department = department;
        this.bio = bio;
    }

    @Override
    public void printInfo() {
        System.out.printf("👨‍🏫 Instructor - ID: %d | Name: %s | Email: %s | Department: %s | Bio: %s%n",
                getUserId(), getName(), getEmail(), department, bio);
    }

    @Override
    public String toString() {
        return String.format("Instructor[userId=%d, name='%s', email='%s', department='%s', bio='%s']",
                getUserId(), getName(), getEmail(), department, bio);
    }

    public String getDepartment() {
        return department;
    }

    public Instructor setDepartment(String department) {
        this.department = department;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public Instructor setBio(String bio) {
        this.bio = bio;
        return this;
    }
}

