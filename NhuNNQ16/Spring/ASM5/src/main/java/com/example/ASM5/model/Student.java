package com.example.ASM5.model;

import java.io.Serializable;


public class Student implements Serializable {
    private long studentId;
    private String name;
    private double gpa;

    public Student() {
    }

    public Student(long studentId, String name, double gpa) {
        this.studentId = studentId;
        this.name = name;
        this.gpa = gpa;
    }

    //. For example: "Student
    //[ID=101, Name=Jane Smith, GPA=3.75]"
    public void printInfo(){
        System.out.println("Student [ID=" + studentId + ", Name=" + name + ", GPA=" + gpa + "]");
    }

    //"Student [studentId=" + studentId + ", name=" + name + ",
    //gpa=" + gpa + "]".

    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", name=" + name + ", gpa=" + gpa + "]";
    }
}
