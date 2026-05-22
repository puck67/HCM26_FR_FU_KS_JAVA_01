package org.example.model;

public class Grade {
    private int id;
    private int studentId;
    private int subjectId;
    private double score;

    private String studentCode;
    private String studentName;
    private String subjectCode;
    private String subjectName;

    public Grade() {
    }

    public Grade(int studentId, int subjectId, double score) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.score = score;
    }

    public Grade(int id, int studentId, int subjectId, double score) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-10s | %-25s | %-5.1f |",
                studentCode, studentName, subjectCode, subjectName, score);
    }
}
