package com.lms.model;

/**
 * Instructor extends LmsUser — thêm thông tin department và bio.
 * Kế thừa Serializable từ LmsUser.
 */
public class Instructor extends LmsUser {

    private static final long serialVersionUID = 2L;

    private final String department;
    private final String bio;

    public Instructor(long userId, String name, String email, String department, String bio) {
        super(userId, name, email);
        this.department = requireNonBlank(department, "department");
        this.bio = bio != null ? bio.strip() : "";
    }

    /**
     * Override displayUserInfo() — thêm thông tin department và bio.
     * Format: "Instructor [ID=1, Name=John Doe, Email=john.doe@fpt.com, Dept=Engineering, Bio=Spring Expert]"
     */
    @Override
    public void displayUserInfo() {
        System.out.printf("Instructor [ID=%d, Name=%s, Email=%s, Dept=%s, Bio=%s]%n",
                userId, name, email, department, bio);
    }

    // ── Getters ────────────────────────────────────────────────────────────────
    public String getDepartment() { return department; }
    public String getBio()        { return bio; }

    @Override
    public String toString() {
        return String.format("Instructor[userId=%d, name='%s', email='%s', dept='%s', bio='%s']",
                userId, name, email, department, bio);
    }
}
