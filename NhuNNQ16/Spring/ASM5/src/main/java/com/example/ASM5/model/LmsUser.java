package com.example.ASM5.model;

import java.io.Serializable;

public class LmsUser implements Serializable {
    private static final long serialVersionUID = 1L;
    protected long userId;
    protected String name;
    protected String email;

    public LmsUser() {
    }

    public LmsUser(long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    //Implements a method void printInfo() that outputs the user's information in the following format:
    //"User [ID=1, Name=John Doe, Email=john.doe@fpt.com]".
    public void printInfo(){
        System.out.println("User [ID=" + userId + ", Name=" + name + ", Email=" + email + "]");
    }


    @Override
    public String toString() {
        return "LmsUser [userId=" + userId + ", name=" + name + ", email=" + email + "]";
    }
}
