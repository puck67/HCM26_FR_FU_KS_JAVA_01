package fa.training.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a Student.
 * Named query: Student.findByName — find students by exact name (case-insensitive).
 */
@Entity
@Table(name = "student")
@NamedQuery(
        name  = "Student.findByName",
        query = "FROM Student s WHERE LOWER(s.name) LIKE LOWER(:name)"
)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    // Owning side of the Many-to-Many relationship
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name               = "student_course",
            joinColumns        = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Student() {}

    public Student(String name, int age) {
        this.name = name;
        this.age  = age;
    }

    // ------------------------------------------------------------------ //
    //  Helper methods for bidirectional sync
    // ------------------------------------------------------------------ //

    public void enrollIn(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }

    public void dropCourse(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }

    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

    public int getAge()                     { return age; }
    public void setAge(int age)             { this.age = age; }

    public Set<Course> getCourses()         { return courses; }
    public void setCourses(Set<Course> c)   { this.courses = c; }

    // ------------------------------------------------------------------ //
    //  toString
    // ------------------------------------------------------------------ //

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', age=" + age + "}";
    }
}
