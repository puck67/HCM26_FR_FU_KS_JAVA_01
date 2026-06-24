package model;

public class Instructor extends LmsUser {
    private static final long serialVersionUID = 2L;

    private String department;
    private String bio;

    public Instructor(long userId, String name, String email, String department, String bio) {
        super(userId, name, email);
        this.department = department;
        this.bio = bio;
    }

    @Override
    public void printInfo() {
        System.out.println(new StringBuilder("Instructor [ID=")
                .append(userId).append(", Name=").append(name)
                .append(", Email=").append(email)
                .append(", Dept=").append(department)
                .append(", Bio=").append(bio).append("]"));
    }

    @Override
    public String toString() {
        return new StringBuilder("Instructor [userId=")
                .append(userId).append(", name=").append(name)
                .append(", email=").append(email)
                .append(", department=").append(department)
                .append(", bio=").append(bio).append("]").toString();
    }
}
