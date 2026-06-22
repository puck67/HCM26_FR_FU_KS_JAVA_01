package com.example.ASM5.model;

import java.io.Serializable;

public class LmsUser implements Serializable {
    private static final long serialVersionUID = 1L;
    protected long uId;
    protected String fullName;
    protected String mail;

    public LmsUser() {
    }

    public LmsUser(long uId, String fullName, String mail) {
        this.uId = uId;
        this.fullName = fullName;
        this.mail = mail;
    }

    public void printInfo(){
        System.out.println("User [ID=" + uId + ", Name=" + fullName + ", Email=" + mail + "]");
    }

    @Override
    public String toString() {
        return "LmsUser [userId=" + uId + ", name=" + fullName + ", email=" + mail + "]";
    }
}
