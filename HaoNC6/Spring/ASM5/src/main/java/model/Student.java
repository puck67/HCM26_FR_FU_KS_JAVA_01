package model;

import java.io.Serializable;

public class Student implements Serializable {
    private long studentId;
    private String name;
    private double gpa;

    public Student() {}

    public Student(long studentId, String name, double gpa) {
        this.studentId = studentId;
        this.name = name;
        this.gpa = gpa;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void printInfo() {
        System.out.println("Student [ID=" + studentId + ", Name=" + name + ", GPA=" + gpa + "]");
    }

    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", name=" + name + ", gpa=" + gpa + "]";
    }
}
