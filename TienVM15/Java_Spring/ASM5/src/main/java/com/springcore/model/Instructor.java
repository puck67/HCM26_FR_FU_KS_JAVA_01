package com.springcore.model;

public class Instructor extends LmsUser {
    private static final long serialVersionUID = 1L;

    private String department;
    private String bio;

    public Instructor(long userId, String name, String email, String department, String bio) {
        super(userId, name, email);
        this.department = department;
        this.bio = bio;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public void printInfo() {
        System.out.println("Instructor [ID=" + userId + ", Name=" + name + ", Email=" + email + ", Dept=" + department + ", Bio=" + bio + "]");
    }
}
