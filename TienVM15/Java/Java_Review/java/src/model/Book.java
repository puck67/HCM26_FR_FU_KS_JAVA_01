package model;

// Forced rebuild comment to trigger Java Language Server refresh
public class Book {

    private String id;
    private String title;
    private String authorEmail;
    private String publisherPhone;
    private double price;
    private int quantity;

    public Book() {
    }

    public Book(String id, String title, String authorEmail, String publisherPhone, double price, int quantity) {
        this.id = id;
        this.title = title;
        this.authorEmail = authorEmail;
        this.publisherPhone = publisherPhone;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getPublisherPhone() {
        return publisherPhone;
    }

    public void setPublisherPhone(String publisherPhone) {
        this.publisherPhone = publisherPhone;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toFileString() {
        return "BK|" + id + "|" + title + "|" + authorEmail + "|" + publisherPhone + "|" + price + "|" + quantity;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", authorEmail=" + authorEmail + ", publisherPhone=" + publisherPhone
                + ", price=" + price + ", quantity=" + quantity + "]";
    }
}
