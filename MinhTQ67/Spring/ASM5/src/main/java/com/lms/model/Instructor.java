package com.lms.model;

/**
 * Task 2: Instructor extends LmsUser - thêm department và bio.
 * Kế thừa Serializable từ LmsUser (không cần implements lại).
 */
public class Instructor extends LmsUser {

    private static final long serialVersionUID = 2L;

    private String department;
    private String bio;

    public Instructor(long userId, String name, String email, String department, String bio) {
        super(userId, name, email); // Gọi constructor của LmsUser
        this.department = department;
        this.bio = bio;
    }

    /**
     * Override printInfo() - thêm thông tin department và bio.
     * Format: "Instructor [ID=1, Name=John Doe, Email=john.doe@fpt.com, Dept=Engineering, Bio=Spring Framework Expert]"
     */
    @Override
    public void printInfo() {
        System.out.println("Instructor [ID=" + userId
                + ", Name=" + name
                + ", Email=" + email
                + ", Dept=" + department
                + ", Bio=" + bio + "]");
    }

    @Override
    public String toString() {
        return "Instructor [userId=" + userId + ", name=" + name + ", email=" + email
                + ", department=" + department + ", bio=" + bio + "]";
    }

    // Getters
    public String getDepartment() { return department; }
    public String getBio()        { return bio; }
}
