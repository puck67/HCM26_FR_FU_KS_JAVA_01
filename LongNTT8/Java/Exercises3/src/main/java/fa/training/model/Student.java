package fa.training.model;

public class Student implements Identifiable {
    private String id;
    private String name;
    private int age;
    private String major;
    private String email;
    private String phone;

    public Student() {
    }

    public Student(String id, String name, int age, String major, String email, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', age=%d, major='%s', email='%s', phone='%s'}", 
                id, name, age, major, email, phone);
    }
}
