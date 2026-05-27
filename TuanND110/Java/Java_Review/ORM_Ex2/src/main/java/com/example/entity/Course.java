package com.example.entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private int credit;
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Student> students = new LinkedHashSet<>();
    public Course() {
    }
    public Course(String title, int credit) {
        this.title = title;
        this.credit = credit;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getCredit() {
        return credit;
    }
    public void setCredit(int credit) {
        this.credit = credit;
    }
    public Set<Student> getStudents() {
        return students;
    }
    public void setStudents(Set<Student> students) {
        this.students = students == null ? new LinkedHashSet<>() : students;
    }
    public void addStudent(Student student) {
        if (student != null && students.add(student)) {
            student.getCourses().add(this);
        }
    }
    public void removeStudent(Student student) {
        if (student != null && students.remove(student)) {
            student.getCourses().remove(this);
        }
    }
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", credit=" + credit +
                ", students=" + students.stream().map(Student::getName).collect(Collectors.toList()) +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id != null && Objects.equals(id, course.id);
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
