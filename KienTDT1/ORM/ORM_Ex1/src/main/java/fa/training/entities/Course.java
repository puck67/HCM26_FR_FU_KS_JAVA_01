package fa.training.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")

public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private int credit;

    @ManyToMany(
            mappedBy = "courses",
            fetch = FetchType.EAGER
    )
    private Set<Student> students = new HashSet<>();

    public Course() {
    }

    public Course(String title, int credit) {
        this.title = title;
        this.credit = credit;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCredit() {
        return credit;
    }

    public Set<Student> getStudents() {
        return students;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "Course{id=" + id +
                ", title='" + title + '\'' +
                ", credit=" + credit +
                '}';
    }
}