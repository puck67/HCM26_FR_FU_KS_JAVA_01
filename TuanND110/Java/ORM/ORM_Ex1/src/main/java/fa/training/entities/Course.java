package fa.training.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="course")
public class Course {
    @Column(name="id", nullable=false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="title", nullable=false)
    private String title;

    @Column(name="credit", nullable=false)
    private int credit;
    //Constructors
    public Course() {
    }
    public Course(String title, int credit) {
        this.title = title;
        this.credit = credit;
    }



    //Getters and Setters
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getCredit() {
        return credit;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCredit(int credit) {
        this.credit = credit;
    }

    //Relationship with Student
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    public Set<Student> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", credit=" + credit +
                '}';
    }

}

