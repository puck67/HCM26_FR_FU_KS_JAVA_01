package org.example.model;

public class Subject {
    private int id;
    private String subjectCode;
    private String name;
    private int credits;


    public int getId() {
        return id;
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-12s | %-25s | %-7d |",
                id, subjectCode, name, credits);
    }

    public Subject() {
    }

    public Subject(String subjectCode, String name, int credits) {
        this.subjectCode = subjectCode;
        this.name = name;
        this.credits = credits;
    }

    public Subject(int id, String subjectCode, String name, int credits) {
        this.id = id;
        this.subjectCode = subjectCode;
        this.name = name;
        this.credits = credits;
    }
}
