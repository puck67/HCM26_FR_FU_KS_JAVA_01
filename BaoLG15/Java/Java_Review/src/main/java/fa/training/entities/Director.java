package fa.training.entities;

import fa.training.util.Validator;

public class Director {

    private int id;
    private String name;

    public Director() {
    }

    public Director(int id, String name) {
        this.id = id;
        setName(name);
    }

    public Director(String name) {
        setName(name);
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
        if (!Validator.isValidName(name)) {
            throw new IllegalArgumentException("Invalid director name");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return "Director{id=%d, name='%s'}".formatted(id, name);
    }
}
