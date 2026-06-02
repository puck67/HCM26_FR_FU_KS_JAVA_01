package org.example.model;

public class Student {
    private int id;
    private String studentCode;
    private String name;
    private String email;
    private String phone;

    public int getId() {
        return id;
    }

    public Student() {
    }

    public Student(String studentCode, String name, String email, String phone) {
        this.studentCode = studentCode;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Student(int id, String studentCode, String name, String email, String phone) {
        this.id = id;
        this.studentCode = studentCode;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    @Override
    public String toString() {
        return String.format("| %-4d | %-12s | %-20s | %-25s | %-12s |",
                id, studentCode, name, email, phone);
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
