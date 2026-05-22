package model;

public class Store {
    private String id;
    private String name;
    private String location;
    private String phone;
    private double rating;

    public Store() {
    }

    public Store(String id, String name, String location, String phone, double rating) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.rating = rating;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("ID: %s, \nName: %s, \nLocation: %s, \nPhone: %s, \nRating: %.1f \n",
            id, name, location, phone, rating);
    }
}

