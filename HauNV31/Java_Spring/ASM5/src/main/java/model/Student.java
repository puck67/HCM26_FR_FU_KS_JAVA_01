package model;

import java.io.Serializable;

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

    public void printInfo() {
        System.out.println(new StringBuilder("Student [ID=")
                .append(studentId).append(", Name=").append(name)
                .append(", GPA=").append(gpa).append("]"));
    }

    @Override
    public String toString() {
        return new StringBuilder("Student [studentId=")
                .append(studentId).append(", name=").append(name)
                .append(", gpa=").append(gpa).append("]").toString();
    }
}
