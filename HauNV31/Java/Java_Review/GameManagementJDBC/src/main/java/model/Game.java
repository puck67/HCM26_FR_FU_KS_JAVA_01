package model;

public class Game {

    private String id;
    private String name;
    private String genre;
    private double price;
    private String developer;

    public Game() {
    }

    public Game(String id, String name, String genre, double price, String developer) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.price = price;
        this.developer = developer;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                ", developer='" + developer + '\'' +
                '}';
    }
}
