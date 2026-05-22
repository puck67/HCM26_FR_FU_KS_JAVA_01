package entities;

public class User {
    private static int counter = 1;
    private String id;
    private String name;

    public User() {
    }

    public User(String name) {
        this.id = "U" + counter;
        counter++;
        this.name = name;
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + '}';
    }    
}
