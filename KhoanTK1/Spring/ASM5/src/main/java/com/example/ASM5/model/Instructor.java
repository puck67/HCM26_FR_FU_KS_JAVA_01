package com.example.ASM5.model;

public class Instructor extends LmsUser {

    private static final long serialVersionUID = 2L;

    private String department;
    private String bio;

    public Instructor() {
        super();
    }

    public Instructor(long uId, String fullName, String mail,
                      String department, String bio) {
        super(uId, fullName, mail);
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
        System.out.println("Instructor [ID=" + uId
                + ", Name=" + fullName
                + ", Email=" + mail
                + ", Dept=" + department
                + ", Bio=" + bio + "]");
    }
}