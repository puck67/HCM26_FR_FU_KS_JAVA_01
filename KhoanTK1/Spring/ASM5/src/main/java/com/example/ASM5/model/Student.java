package com.example.ASM5.model;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private long sId;
    private String fullName;
    private double gradePoint;

    public Student() {
    }

    public Student(long sId, String fullName, double gradePoint) {
        this.sId = sId;
        this.fullName = fullName;
        this.gradePoint = gradePoint;
    }

    public void printInfo(){
        System.out.println("Student [ID=" + sId + ", Name=" + fullName + ", GPA=" + gradePoint + "]");
    }

    @Override
    public String toString() {
        return "Student [studentId=" + sId + ", name=" + fullName + ", gpa=" + gradePoint + "]";
    }
}
