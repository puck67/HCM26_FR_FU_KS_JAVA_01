package entities;

import java.util.Date;

public class Cat {
    private String id;
    private String name;
    private Date birthDay;
    private Owner owner;

    public Cat() {
    }

    public Cat(String id, String name, Date birthDay, Owner owner) {
        this.id = id;
        this.name = name;
        this.birthDay = birthDay;
        this.owner = owner;
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

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", birthDay=" + birthDay +
                ", owner=" + owner +
                '}';
    }
}
