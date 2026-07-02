package com.fpt.lms.model;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private long studentId;
    private String name;
    private double gpa;

    public Student() {}

    public Student(long studentId, String name, double gpa) {
        this.studentId = studentId;
        this.name = name;
        this.gpa = gpa;
    }

    public void printInfo() {
        System.out.printf("🎓 Student - ID: %d | Name: %s | GPA: %.2f%n", studentId, name, gpa);
    }

    @Override
    public String toString() {
        return String.format("Student[studentId=%d, name='%s', gpa=%.2f]", studentId, name, gpa);
    }

    public long getStudentId() {
        return studentId;
    }

    public Student setStudentId(long studentId) {
        this.studentId = studentId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public double getGpa() {
        return gpa;
    }

    public Student setGpa(double gpa) {
        this.gpa = gpa;
        return this;
    }
}

