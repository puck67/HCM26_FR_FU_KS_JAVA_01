package org.fsa_2026.enities;

import java.util.Date;

public class Learner {
    private int id;
    private String studentName;
    private String phone;
    private String email;
    private Date birthDate;
    private String classRoom;

    public Learner() {
    }
    public Learner(int id, String studentName, String phone, String email, Date birthDate, String classRoom) {
        this.id = id;
        this.studentName = studentName;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.classRoom = classRoom;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    public String getClassRoom() {
        return classRoom;
    }
    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }


    public String toString() {
        return id + " " + studentName + " " + phone + " " + email + " " + birthDate + " " + classRoom;
    }


}
