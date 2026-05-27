package com.example.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student")
@NamedQuery(name = "Student.findByName", query = "FROM Student WHERE name = :name")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int age;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    public Student() {
    }

    public Student(String name, int age) {
        setName(name);
        setAge(age);
    }

    public Student(int id, String name, int age) {
        this.id = id;
        setName(name);
        setAge(age);
    }

    public void addCourse(Course course) {
        if (course != null && !this.courses.contains(course)) {
            this.courses.add(course);
            course.getStudents().add(this);
        }
    }

    public void removeCourse(Course course) {
        if (course != null && this.courses.contains(course)) {
            this.courses.remove(course);
            course.getStudents().remove(this);
        }
    }

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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new IllegalArgumentException("Student name must be between 2 and 100 characters");
        }
        this.name = name.trim();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 5 || age > 120) {
            throw new IllegalArgumentException("Student age must be between 5 and 120");
        }
        this.age = age;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', age=" + age + '}';
    }
}

