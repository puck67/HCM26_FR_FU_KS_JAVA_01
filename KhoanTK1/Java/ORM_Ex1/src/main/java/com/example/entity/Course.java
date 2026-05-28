package com.example.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false)
    private int credit;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    public Course() {
    }

    public Course(String title, int credit) {
        setTitle(title);
        setCredit(credit);
    }

    public Course(int id, String title, int credit) {
        this.id = id;
        setTitle(title);
        setCredit(credit);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be null or empty");
        }
        if (title.length() < 2 || title.length() > 150) {
            throw new IllegalArgumentException("Course title must be between 2 and 150 characters");
        }
        this.title = title.trim();
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        if (credit <= 0 || credit > 10) {
            throw new IllegalArgumentException("Course credit must be greater than 0 and less than or equal to 10");
        }
        this.credit = credit;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Course{id=" + id + ", title='" + title + "', credit=" + credit + '}';
    }
}

