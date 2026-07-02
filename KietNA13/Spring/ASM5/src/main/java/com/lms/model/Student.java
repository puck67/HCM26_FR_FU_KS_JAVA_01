package com.lms.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Student — lớp độc lập, KHÔNG extends LmsUser (theo yêu cầu đề bài).
 * Implements Serializable riêng để ghi/đọc file students.dat.
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 3L;

    private final long studentId;
    private final String name;
    private final double gpa;

    public Student(long studentId, String name, double gpa) {
        if (studentId <= 0) throw new IllegalArgumentException("studentId must be positive, got: " + studentId);
        this.studentId = studentId;
        this.name = requireNonBlank(name, "name");
        if (gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0, got: " + gpa);
        }
        this.gpa = gpa;
    }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Field '" + field + "' must not be blank");
        }
        return value.strip();
    }

    /**
     * In thông tin student theo format:
     * "Student [ID=101, Name=Jane Smith, GPA=3.75]"
     */
    public void displayStudentInfo() {
        System.out.printf("Student [ID=%d, Name=%s, GPA=%.2f]%n", studentId, name, gpa);
    }

    // ── Getters ────────────────────────────────────────────────────────────────
    public long getStudentId() { return studentId; }
    public String getName()    { return name; }
    public double getGpa()     { return gpa; }

    @Override
    public String toString() {
        return String.format("Student[studentId=%d, name='%s', gpa=%.2f]", studentId, name, gpa);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        return studentId == ((Student) o).studentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
