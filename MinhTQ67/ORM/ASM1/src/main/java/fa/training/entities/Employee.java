package fa.training.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class mapped to EMPLOYEE table in fadb database.
 *
 * Table structure:
 *   ID         INT          PRIMARY KEY (auto-increment)
 *   FIRST_NAME VARCHAR(50)  NOT NULL
 *   LAST_NAME  VARCHAR(50)  NOT NULL
 *
 * Note: Class name follows Java naming convention (no underscores).
 *       Column names use underscore as required by DB design.
 */
@Entity
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;

    // ============ Constructors ============

    public Employee() {}

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // ============ Getters & Setters ============

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // ============ toString ============

    @Override
    public String toString() {
        return "Employee{"
                + "id=" + id
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + '}';
    }
}
