package org.example.model;


public class Student {
    private String id;
    private String name;
    private String email;
    private String phone;
    private double gpa;



    public Student() {}

    public Student(String id, String name, String email, String phone, double gpa) {
        this.id    = id;
        this.name  = name;
        this.email = email;
        this.phone = phone;
        this.gpa   = gpa;
    }


    public String getId()               { return id; }
    public void   setId(String id)      { this.id = id; }

    public String getName()             { return name; }
    public void   setName(String name)  { this.name = name; }

    public String getEmail()              { return email; }
    public void   setEmail(String email)  { this.email = email; }

    public String getPhone()              { return phone; }
    public void   setPhone(String phone)  { this.phone = phone; }

    public double getGpa()              { return gpa; }
    public void   setGpa(double gpa)    { this.gpa = gpa; }


    @Override
    public String toString() {
        return String.format(
            "| %-10s | %-25s | %-30s | %-12s | %-5.2f |",
            id, name, email, phone, gpa
        );
    }
}
