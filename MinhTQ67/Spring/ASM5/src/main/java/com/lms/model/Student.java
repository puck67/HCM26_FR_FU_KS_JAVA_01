package com.lms.model;

import java.io.Serializable;

/**
 * Task 3: Student - lớp độc lập, KHÔNG extends LmsUser (theo yêu cầu đề bài).
 * Implements Serializable riêng để ghi/đọc file students.dat.
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 3L;

    private long studentId;
    private String name;
    private double gpa;

    public Student(long studentId, String name, double gpa) {
        this.studentId = studentId;
        this.name = name;
        this.gpa = gpa;
    }

    /**
     * In thông tin student theo format yêu cầu:
     * "Student [ID=101, Name=Jane Smith, GPA=3.75]"
     */
    public void printInfo() {
        System.out.println("Student [ID=" + studentId + ", Name=" + name + ", GPA=" + gpa + "]");
    }

    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", name=" + name + ", gpa=" + gpa + "]";
    }

    // Getters
    public long getStudentId() { return studentId; }
    public String getName()    { return name; }
    public double getGpa()     { return gpa; }
}
