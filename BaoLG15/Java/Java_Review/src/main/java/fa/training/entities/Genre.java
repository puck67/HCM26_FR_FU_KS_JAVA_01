package fa.training.entities;

import fa.training.util.Validator;

public class Genre {

    private int id;
    private String name;

    public Genre() {
    }

    public Genre(int id, String name) {
        this.id = id;
        setName(name);
    }

    public Genre(String name) {
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
            throw new IllegalArgumentException("Invalid genre name");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genre{id=%d, name='%s'}".formatted(id, name);
    }
}
