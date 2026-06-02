package fa.training.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a Course.
 */
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int credit;

    // Inverse (non-owning) side of the Many-to-Many relationship
    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private Set<Student> students = new HashSet<>();

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Course() {}

    public Course(String title, int credit) {
        this.title  = title;
        this.credit = credit;
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public String getTitle()                   { return title; }
    public void setTitle(String title)         { this.title = title; }

    public int getCredit()                     { return credit; }
    public void setCredit(int credit)          { this.credit = credit; }

    public Set<Student> getStudents()          { return students; }
    public void setStudents(Set<Student> s)    { this.students = s; }

    // ------------------------------------------------------------------ //
    //  toString
    // ------------------------------------------------------------------ //

    @Override
    public String toString() {
        return "Course{id=" + id + ", title='" + title + "', credit=" + credit + "}";
    }
}
