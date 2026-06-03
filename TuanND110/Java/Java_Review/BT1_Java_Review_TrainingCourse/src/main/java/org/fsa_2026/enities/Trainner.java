package org.fsa_2026.enities;

import java.util.Date;

public class Trainner {
    private int id;
    private String trainnerName;
    private String phone;
    private String email;
    private Date birthDate;
    private String className;

    public Trainner() {

    }
    public Trainner(int id, String trainnerName, String phone, String email, Date birthDate, String className) {
        this.id = id;
        this.trainnerName = trainnerName;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.className = className;
    }
    //Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTrainnerName() {
        return trainnerName;
    }
    public void setTrainnerName(String trainnerName) {
        this.trainnerName = trainnerName;
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
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public void setClass(String className) {
        this.className = className;
    }

    public String toString() {
        return id + " " + trainnerName + " " + phone + " " + email + " " + birthDate + " " + className;
    }
}
