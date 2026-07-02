package com.lms.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Course")
@IdClass(CourseId.class)
public class Course {

    @Id
    @Column(name = "course_code", nullable = false, length = 50)
    private String courseCode;

    @Id
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "course_name", nullable = false, length = 255)
    private String courseName;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "instructor", length = 100)
    private String instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    public Course() {}

    public Course(String courseCode, LocalDate startDate, String courseName, String category, String instructor) {
        this.courseCode = courseCode;
        this.startDate = startDate;
        this.courseName = courseName;
        this.category = category;
        this.instructor = instructor;
    }

    // Getters & Setters
    public String getCourseCode() {
        return courseCode;
    }
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructor() {
        return instructor;
    }
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }
    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
